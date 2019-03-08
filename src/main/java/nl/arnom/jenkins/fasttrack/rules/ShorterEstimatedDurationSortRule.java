package nl.arnom.jenkins.fasttrack.rules;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

public class ShorterEstimatedDurationSortRule extends AbstractJobSortRule {

    @DataBoundConstructor
    public ShorterEstimatedDurationSortRule() {
    }

    @Override
    protected SortResult sortUsingJobs(Job<?, ?> first, Job<?, ?> second) {
        Long firstDuration = getEstimatedDuration(first);
        Long secondDuration = getEstimatedDuration(second);

        SortResult result = preferNotNull(firstDuration, secondDuration);
        if (result != null) {
            return result;
        }

        long delta = firstDuration - secondDuration;
        if (delta < 0) {
            return SortResult.FIRST;
        }
        if (delta > 0) {
            return SortResult.SECOND;
        }
        return SortResult.NO_PREFERENCE;
    }

    private Long getEstimatedDuration(Job<?, ?> job) {
        if (job.getLastCompletedBuild() != null) {
            return job.getEstimatedDuration();
        }
        return null;
    }

    @Extension
    @Symbol("shorterEstimatedDurationSortRule")
    public static class DescriptorImpl extends Descriptor<SortRule> {
        @Nonnull
        public String getDisplayName() {
            return "Prefer builds of which the estimated duration is less";
        }
    }
}

package nl.arnom.jenkins.fasttrack.rules;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Job;
import hudson.util.FormValidation;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class ShorterEstimatedDurationSortRule extends AbstractJobSortRule {

    private final int minimumDeltaSeconds;

    public ShorterEstimatedDurationSortRule() {
        this(30);
    }

    @DataBoundConstructor
    public ShorterEstimatedDurationSortRule(int minimumDeltaSeconds) {
        this.minimumDeltaSeconds = minimumDeltaSeconds;
    }

    public int getMinimumDeltaSeconds() {
        return minimumDeltaSeconds;
    }

    @Override
    protected SortResult sortUsingJobs(Job<?, ?> first, Job<?, ?> second) {
        Long firstDuration = getEstimatedDuration(first);
        Long secondDuration = getEstimatedDuration(second);

        if (firstDuration == null || secondDuration == null) {
            return SortResult.NO_PREFERENCE;
        }

        long delta = firstDuration - secondDuration;
        if (delta == 0 || (Math.abs(delta) < minimumDeltaSeconds)) {
            return SortResult.NO_PREFERENCE;
        }

        if (delta < 0) {
            return SortResult.FIRST;
        }
        return SortResult.SECOND;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ShorterEstimatedDurationSortRule that = (ShorterEstimatedDurationSortRule) o;
        return minimumDeltaSeconds == that.minimumDeltaSeconds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minimumDeltaSeconds);
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

        public FormValidation doCheckMinimumDeltaSeconds(@QueryParameter String value) {
            try {
                int input = Integer.parseUnsignedInt(value);
                if (input >= 0) {
                    return FormValidation.ok();
                }
                return FormValidation.error("Valid positive number or zero required.");
            } catch (Throwable e) {
                return FormValidation.error(e, "Valid positive number or zero required.");
            }
        }

        @Nonnull
        public String getDisplayName() {
            return "Prefer builds of which the estimated duration is less";
        }
    }
}

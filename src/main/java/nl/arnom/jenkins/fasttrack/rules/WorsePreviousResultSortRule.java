package nl.arnom.jenkins.fasttrack.rules;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Result;
import hudson.model.Run;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

public class WorsePreviousResultSortRule extends AbstractLastCompletedBuildSortRule {

    @DataBoundConstructor
    public WorsePreviousResultSortRule() {
    }


    @Override
    protected SortResult sortUsingLastCompletedBuild(Run<?, ?> first, Run<?, ?> second) {
        Result firstResult = first.getResult();
        Result secondResult = second.getResult();

        SortResult result = preferNotNull(firstResult, secondResult);
        if (result != null) {
            return result;
        }

        if (firstResult.equals(secondResult)) {
            return SortResult.NO_PREFERENCE;
        }

        if (firstResult.isWorseThan(secondResult)) {
            return SortResult.FIRST;
        }

        return SortResult.SECOND;
    }

    @Extension
    @Symbol("worsePreviousResultSortRule")
    public static class DescriptorImpl extends Descriptor<SortRule> {
        @Nonnull
        public String getDisplayName() {
            return "Prefer builds of which the last build has the worst result";
        }
    }
}

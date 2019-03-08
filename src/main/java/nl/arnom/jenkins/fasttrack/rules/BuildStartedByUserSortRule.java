package nl.arnom.jenkins.fasttrack.rules;

import hudson.Extension;
import hudson.model.Cause;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

public class BuildStartedByUserSortRule extends CauseCountSortRule {

    @DataBoundConstructor
    public BuildStartedByUserSortRule() {
    }

    @Override
    protected boolean isAcceptedCause(Cause cause) {
        return (cause instanceof Cause.UserIdCause);
    }

    @Extension
    @Symbol("buildStartedByUserSortRule")
    public static class DescriptorImpl extends Descriptor<SortRule> {
        @Nonnull
        public String getDisplayName() {
            return "Prefer builds started by a user";
        }
    }
}

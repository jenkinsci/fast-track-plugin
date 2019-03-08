package nl.arnom.jenkins.fasttrack.rules;

import hudson.Extension;
import hudson.model.Cause;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import javax.annotation.Nonnull;

public class UpstreamCauseSortRule extends CauseCountSortRule {

    @DataBoundConstructor
    public UpstreamCauseSortRule() {
    }

    @Override
    protected boolean isAcceptedCause(Cause cause) {
        return (cause instanceof Cause.UpstreamCause);
    }

    @Extension
    @Symbol("upstreamCauseSortRule")
    public static class DescriptorImpl extends Descriptor<SortRule> {
        @Nonnull
        public String getDisplayName() {
            return "Prefer builds started by an upstream build";
        }
    }
}

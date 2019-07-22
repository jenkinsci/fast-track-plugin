package nl.arnom.jenkins.fasttrack.rules;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.model.Queue;
import hudson.util.FormValidation;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import javax.annotation.Nonnull;
import java.util.Objects;

public class LongerWaitTimeSortRule extends SortRule {
    private final int durationMinutes;
    private final int deltaMinutes;

    @DataBoundConstructor
    public LongerWaitTimeSortRule(int durationMinutes, int deltaMinutes) {
        this.durationMinutes = durationMinutes;
        this.deltaMinutes = deltaMinutes;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public int getDeltaMinutes() {
        return deltaMinutes;
    }

    @Override
    public SortResult sort(Queue.BuildableItem first, Queue.BuildableItem second) {
        long duration = durationMinutes * 60;
        long delta = deltaMinutes * 60;
        long now = System.currentTimeMillis() / 1000L;

        long firstTime = first.getInQueueSince();
        long secondTime = second.getInQueueSince();

        if (Math.abs(firstTime - secondTime) < delta) {
            return SortResult.NO_PREFERENCE;
        }

        long firstWaitTime = now - firstTime;
        long secondWaitTime = now - secondTime;

        if (firstWaitTime > secondWaitTime) {
            if (firstWaitTime > duration) {
                return SortResult.FIRST;
            }
        } else if ((secondWaitTime > firstWaitTime) && (secondWaitTime > duration)) {
            return SortResult.SECOND;
        }

        return SortResult.NO_PREFERENCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LongerWaitTimeSortRule)) return false;
        if (!super.equals(o)) return false;
        LongerWaitTimeSortRule that = (LongerWaitTimeSortRule) o;
        return durationMinutes == that.durationMinutes &&
                deltaMinutes == that.deltaMinutes;
    }

    @Override
    public int hashCode() {
        return Objects.hash(durationMinutes, deltaMinutes);
    }

    @Extension
    @Symbol("longerWaitTimeSortRule")
    public static class DescriptorImpl extends Descriptor<SortRule> {
        @Nonnull
        public String getDisplayName() {
            return "Prefer builds that have been waiting for a long time";
        }


        public FormValidation doCheckDurationMinutes(@QueryParameter String value) {
            try {
                int input = Integer.parseUnsignedInt(value);
                if (input > 0) {
                    return FormValidation.ok();
                }
                return FormValidation.error("Number greater than one required.");
            } catch (Throwable e) {
                return FormValidation.error(e, "Valid positive number required.");
            }
        }

        public FormValidation doCheckDeltaMinutes(@QueryParameter String value, @QueryParameter String durationMinutes) {
            Integer duration = null;
            try {
                duration = Integer.parseUnsignedInt(durationMinutes);
            } catch (Throwable e) {
                // ignored
            }
            try {
                int input = Integer.parseUnsignedInt(value);
                if (duration != null && duration > 0 && input >= duration) {
                    return FormValidation.warning("It is recommended to use a minimum difference that is less than the minimum duration. ");
                }
                if (input > 0) {
                    return FormValidation.ok();
                }
                return FormValidation.warning("It is recommended to use a minimum difference greater than 0 minutes.");
            } catch (Throwable e) {
                return FormValidation.error(e, "Valid positive number required.");
            }
        }
    }
}

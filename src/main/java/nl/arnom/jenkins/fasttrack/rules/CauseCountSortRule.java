package nl.arnom.jenkins.fasttrack.rules;

import hudson.model.Cause;
import hudson.model.Queue;

import java.util.Collection;

public abstract class CauseCountSortRule extends SortRule {

    @Override
    public SortResult sort(Queue.BuildableItem first, Queue.BuildableItem second) {
        int countFirst = countCauses(first.getCauses());
        int countSecond = countCauses(second.getCauses());

        if (countFirst == countSecond) {
            return SortResult.NO_PREFERENCE;
        }
        if (countFirst > countSecond) {
            return SortResult.FIRST;
        }
        return SortResult.SECOND;
    }

    protected int countCauses(Collection<Cause> causes) {
        int result = 0;
        for (Cause c : causes) {
            if (isAcceptedCause(c)) {
                ++result;
            }
        }
        return result;
    }

    protected abstract boolean isAcceptedCause(Cause cause);
}

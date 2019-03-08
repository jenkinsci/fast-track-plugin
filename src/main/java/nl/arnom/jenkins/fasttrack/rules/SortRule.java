package nl.arnom.jenkins.fasttrack.rules;

import hudson.ExtensionPoint;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Queue;

public abstract class SortRule extends AbstractDescribableImpl<SortRule> implements ExtensionPoint {
    public Descriptor<SortRule> getDescriptor() {
        return super.getDescriptor();
    }

    public abstract SortResult sort(Queue.BuildableItem first, Queue.BuildableItem second);

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SortRule) {
            // By default assume that SortRules don't have any properties,
            // so they can be considered equal if they are of the same type.
            return this.getClass().equals(obj.getClass());
        }
        return false;
    }

    public enum SortResult {
        NO_PREFERENCE,
        FIRST,
        SECOND
    }
}

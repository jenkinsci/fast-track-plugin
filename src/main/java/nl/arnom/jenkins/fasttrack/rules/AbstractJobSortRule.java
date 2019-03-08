package nl.arnom.jenkins.fasttrack.rules;

import hudson.model.Job;
import hudson.model.Queue;

public abstract class AbstractJobSortRule extends SortRule {

    protected abstract SortResult sortUsingJobs(Job<?, ?> first, Job<?, ?> second);

    @Override
    public SortResult sort(Queue.BuildableItem first, Queue.BuildableItem second) {
        Job<?, ?> firstJob = convertToJob(first);
        Job<?, ?> secondJob = convertToJob(second);

        SortResult result = preferNotNull(firstJob, secondJob);
        if (result != null) {
            return result;
        }

        return sortUsingJobs(firstJob, secondJob);
    }

    private Job<?, ?> convertToJob(Queue.BuildableItem item) {
        if (item.task instanceof Job<?, ?>) {
            return (Job<?, ?>) item.task;
        }
        return null;
    }

    protected SortResult preferNotNull(Object first, Object second) {
        if (first == null) {
            if (second == null) {
                return SortResult.NO_PREFERENCE;
            }
            return SortResult.SECOND;
        }

        if (second == null) {
            return SortResult.FIRST;
        }

        return null;
    }
}

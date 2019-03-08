package nl.arnom.jenkins.fasttrack.rules;

import hudson.model.Job;
import hudson.model.Queue;
import hudson.model.Run;

public abstract class AbstractLastCompletedBuildSortRule extends AbstractJobSortRule {

    protected abstract SortResult sortUsingLastCompletedBuild(Run<?, ?> first, Run<?, ?> second);

    protected SortResult sortUsingJobs(Job<?, ?> first, Job<?, ?> second) {
        Run<?, ?> firstRun = first.getLastCompletedBuild();
        Run<?, ?> secondRun = first.getLastCompletedBuild();

        SortResult result = preferNotNull(firstRun, secondRun);
        if (result != null) {
            return result;
        }

        return sortUsingLastCompletedBuild(firstRun, secondRun);
    }

    @Override
    public SortResult sort(Queue.BuildableItem first, Queue.BuildableItem second) {
        Job<?, ?> firstJob = null;
        if (first.task instanceof Job<?, ?>) {
            firstJob = (Job<?, ?>) first.task;
        }

        Job<?, ?> secondJob = null;
        if (second.task instanceof Job<?, ?>) {
            secondJob = (Job<?, ?>) second.task;
        }

        if (firstJob == null) {
            if (secondJob == null) {
                return SortResult.NO_PREFERENCE;
            }
            return SortResult.SECOND;
        }

        if (secondJob == null) {
            return SortResult.FIRST;
        }

        return sortUsingJobs(firstJob, secondJob);
    }
}

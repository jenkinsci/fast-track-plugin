package nl.arnom.jenkins.fasttrack;

import hudson.Extension;
import hudson.init.Initializer;
import hudson.model.Job;
import hudson.model.Queue;
import hudson.model.queue.AbstractQueueSorterImpl;
import hudson.model.queue.QueueSorter;
import jenkins.model.Jenkins;
import nl.arnom.jenkins.fasttrack.rules.SortRule;

import java.util.List;
import java.util.logging.Logger;

import static hudson.init.InitMilestone.JOB_LOADED;

@Extension
public class ConfigurableQueueSorter extends AbstractQueueSorterImpl {

    private final static Logger LOGGER = Logger.getLogger(ConfigurableQueueSorter.class.getName());

    @Initializer(after = JOB_LOADED)
    public static void setupQueueSorter() {
        LOGGER.info("Setting up " + ConfigurableQueueSorter.class.getSimpleName());
        Jenkins jenkins = Jenkins.getInstanceOrNull();
        if (jenkins != null) {
            Queue q = jenkins.getQueue();
            q.setSorter(get());
        } else {
            LOGGER.severe("Cannot get instance of Jenkins");
        }
    }

    public static ConfigurableQueueSorter get() {
        return QueueSorter.all().get(ConfigurableQueueSorter.class);
    }

    private static void LogPreference(SortRule rule, Queue.BuildableItem preferredOne, Queue.BuildableItem otherOne) {
        LOGGER.fine("Rule " + rule.getClass().getSimpleName() + " prefers " + GetNameOfItem(preferredOne) + " over " + GetNameOfItem(otherOne));
    }

    private static void LogNoPreference(SortRule rule, Queue.BuildableItem first, Queue.BuildableItem second) {
        LOGGER.finer("Rule " + rule.getClass().getSimpleName() + " has no preference for either of " + GetNameOfItem(first) + " or " + GetNameOfItem(second));
    }

    private static String GetNameOfItem(Queue.BuildableItem item) {
        if (item.task != null) {
            StringBuilder builder = new StringBuilder();
            if (item.task.getFullDisplayName() != null) {
                builder.append(item.task.getFullDisplayName());
            } else {
                builder.append(item.task.getClass().getName());
            }
            if (item.task instanceof Job<?, ?>) {
                Job<?, ?> job = (Job<?, ?>) item.task;
                builder.append(" #");
                builder.append(job.getNextBuildNumber());
            }
            return builder.toString();
        } else {
            return item.toString();
        }
    }

    @Override
    public int compare(Queue.BuildableItem lhs, Queue.BuildableItem rhs) {
        List<SortRule> rules = FastTrackConfiguration.get().getSortRuleCollection();
        for (SortRule rule : rules) {
            SortRule.SortResult result = rule.sort(lhs, rhs);
            switch (result) {
                case FIRST:
                    LogPreference(rule, lhs, rhs);
                    return -1;
                case SECOND:
                    LogPreference(rule, rhs, lhs);
                    return 1;
                default:
                    LogNoPreference(rule, lhs, rhs);
                    break;
            }
        }

        long deltaWaitTime = lhs.getInQueueSince() - rhs.getInQueueSince();
        if (deltaWaitTime < 0) {
            return -1;
        }
        if (deltaWaitTime > 0) {
            return 1;
        }

        int deltaCauses = lhs.getCauses().size() - rhs.getCauses().size();
        if (deltaCauses < 0) {
            return 1;
        }
        if (deltaCauses > 0) {
            return -1;
        }

        LOGGER.warning("FAILED TO SORT " + lhs.getDisplayName() + " AND " + rhs.getDisplayName());

        return super.compare(lhs, rhs);
    }
}

package nl.arnom.jenkins.fasttrack;

import com.google.common.collect.Lists;
import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import jenkins.model.GlobalConfigurationCategory;
import nl.arnom.jenkins.fasttrack.rules.*;
import org.jenkinsci.Symbol;

import java.util.Collections;
import java.util.List;

@Extension
@Symbol("fastTrack")
public class FastTrackConfiguration extends GlobalConfiguration {
    private List<SortRule> sortRules = Lists.newArrayList();

    public FastTrackConfiguration() {
        load();

        if (sortRules.isEmpty()) {
            sortRules.add(new UpstreamCauseSortRule());
            sortRules.add(new BuildStartedByUserSortRule());
            sortRules.add(new LongerWaitTimeSortRule(90, 15));
            sortRules.add(new WorsePreviousResultSortRule());
            sortRules.add(new ShorterEstimatedDurationSortRule());
        }
    }

    public static FastTrackConfiguration get() {
        return GlobalConfiguration.all().get(FastTrackConfiguration.class);
    }

    public SortRule[] getSortRules() {
        return sortRules.toArray(new SortRule[0]);
    }

    public void setSortRules(final SortRule[] sortRules) {
        if (sortRules != null) {
            this.sortRules.clear();
            for (SortRule rule : sortRules) {
                if (!this.sortRules.contains(rule)) {
                    this.sortRules.add(rule);
                }
            }
        }
        save();
    }

    public List<SortRule> getSortRuleCollection() {
        return Collections.unmodifiableList(sortRules);
    }

    @Override
    public GlobalConfigurationCategory getCategory() {
        return GlobalConfigurationCategory.get(GlobalConfigurationCategory.Unclassified.class);
    }
}

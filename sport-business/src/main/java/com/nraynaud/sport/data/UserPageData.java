package com.nraynaud.sport.data;

import com.nraynaud.sport.Group;

import java.util.Collection;

public class UserPageData {
    public final Collection<ConversationSumary> privateMessageReceivers;
    private final StatisticsData statisticsData;
    public final Collection<Group> groupMembership;

    public UserPageData(final Collection<ConversationSumary> privateMessageReceivers,
                        final StatisticsData statisticsData, final Collection<Group> groupMembership) {
        this.statisticsData = statisticsData;
        this.privateMessageReceivers = privateMessageReceivers;
        this.groupMembership = groupMembership;
    }

    public StatisticsData getStatisticsData() {
        return statisticsData;
    }
}

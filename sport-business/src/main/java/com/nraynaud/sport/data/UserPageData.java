package com.nraynaud.sport.data;

import java.util.Collection;

public class UserPageData {
    public final Collection<ConversationSumary> privateMessageReceivers;
    private final StatisticsData statisticsData;

    public UserPageData(final Collection<ConversationSumary> privateMessageReceivers,
                        final StatisticsData statisticsData) {
        this.statisticsData = statisticsData;
        this.privateMessageReceivers = privateMessageReceivers;
    }

    public StatisticsData getStatisticsData() {
        return statisticsData;
    }
}

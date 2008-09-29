package com.nraynaud.sport.data;

import java.util.Collection;

public class UserPageData {
    public final Collection<ConversationSummary> privateMessageReceivers;
    private final StatisticsData<DisciplineData.Count> statisticsData;
    public final Collection<GroupData> groupMembership;

    public UserPageData(final Collection<ConversationSummary> privateMessageReceivers,
                        final StatisticsData<DisciplineData.Count> statisticsData, final Collection<GroupData> groupMembership) {
        this.statisticsData = statisticsData;
        this.privateMessageReceivers = privateMessageReceivers;
        this.groupMembership = groupMembership;
    }

    public StatisticsData<DisciplineData.Count> getStatisticsData() {
        return statisticsData;
    }
}

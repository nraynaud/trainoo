package com.nraynaud.sport.data;

import com.nraynaud.sport.PublicMessage;

public class GlobalWorkoutsPageData {
    public final PaginatedCollection<PublicMessage> recentMessages;
    public final StatisticsData<DisciplineData.Count> statisticsData;

    public GlobalWorkoutsPageData(final PaginatedCollection<PublicMessage> recentMessages,
                                  final StatisticsData<DisciplineData.Count> statisticsData) {
        this.statisticsData = statisticsData;
        this.recentMessages = recentMessages;
    }
}

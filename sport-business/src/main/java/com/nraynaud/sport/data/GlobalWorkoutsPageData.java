package com.nraynaud.sport.data;

import com.nraynaud.sport.PublicMessage;

import java.util.List;

public class GlobalWorkoutsPageData {
    public final List<PublicMessage> recentMessages;
    public final StatisticsData statisticsData;

    public GlobalWorkoutsPageData(final List<PublicMessage> recentMessages, final StatisticsData statisticsData) {
        this.statisticsData = statisticsData;
        this.recentMessages = recentMessages;
    }
}

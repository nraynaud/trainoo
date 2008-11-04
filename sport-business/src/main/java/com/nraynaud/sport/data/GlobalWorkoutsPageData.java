package com.nraynaud.sport.data;

import com.nraynaud.sport.PublicMessage;

public class GlobalWorkoutsPageData {
    public final PaginatedCollection<PublicMessage> recentMessages;
    public final WorkoutsData<DisciplineData.Count> workoutsData;

    public GlobalWorkoutsPageData(final PaginatedCollection<PublicMessage> recentMessages,
                                  final WorkoutsData<DisciplineData.Count> workoutsData) {
        this.workoutsData = workoutsData;
        this.recentMessages = recentMessages;
    }
}

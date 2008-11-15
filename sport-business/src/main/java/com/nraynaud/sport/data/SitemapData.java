package com.nraynaud.sport.data;

public class SitemapData {
    public final Iterable<Number> userIds;
    public final Iterable<Number> workoutIds;
    public final Iterable<Number> groupIds;

    public SitemapData(final Iterable<Number> userIds, final Iterable<Number> workoutIds,
                       final Iterable<Number> groupIds) {
        this.userIds = userIds;
        this.workoutIds = workoutIds;
        this.groupIds = groupIds;
    }
}

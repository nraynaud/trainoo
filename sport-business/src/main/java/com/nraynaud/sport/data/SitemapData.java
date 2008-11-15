package com.nraynaud.sport.data;

public class SitemapData {
    public final Iterable<Number> userIds;
    public final Iterable<Number> workoutIds;

    public SitemapData(final Iterable<Number> userIds, final Iterable<Number> workoutIds) {
        this.userIds = userIds;
        this.workoutIds = workoutIds;
    }
}

package com.nraynaud.sport.data;

import java.util.Collection;

public class UserPageData {
    public final Collection<ConversationSummary> privateMessageReceivers;
    private final WorkoutsData<DisciplineData.Count> workoutsData;
    public final Collection<GroupData> groupMembership;

    public UserPageData(final Collection<ConversationSummary> privateMessageReceivers,
                        final WorkoutsData<DisciplineData.Count> workoutsData,
                        final Collection<GroupData> groupMembership) {
        this.workoutsData = workoutsData;
        this.privateMessageReceivers = privateMessageReceivers;
        this.groupMembership = groupMembership;
    }

    public WorkoutsData<DisciplineData.Count> getStatisticsData() {
        return workoutsData;
    }
}

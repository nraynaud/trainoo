package com.nraynaud.sport.data;

import com.nraynaud.sport.Workout;

import java.util.Collection;
import java.util.List;

public class UserPageData extends StatisticsPageData {
    public final Collection<ConversationSumary> privateMessageReceivers;

    public UserPageData(final PaginatedCollection<Workout> workouts, final Double globalDistance,
                        final List<DisciplineDistance> distanceByDisciplines,
                        final Collection<ConversationSumary> privateMessageReceivers) {
        super(workouts, globalDistance, distanceByDisciplines);
        this.privateMessageReceivers = privateMessageReceivers;
    }
}

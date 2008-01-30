package com.nraynaud.sport.data;

import com.nraynaud.sport.Workout;

import java.util.Collection;
import java.util.List;

public class UserPageData extends StatisticsPageData {
    public final Collection<String> privateMessageReceivers;

    public UserPageData(final List<Workout> workouts, final Double globalDistance,
                        final List<DisciplineDistance> distanceByDisciplines,
                        final Collection<String> privateMessageReceivers) {
        super(workouts, globalDistance, distanceByDisciplines);
        this.privateMessageReceivers = privateMessageReceivers;
    }
}

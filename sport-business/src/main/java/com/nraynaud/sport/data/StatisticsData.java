package com.nraynaud.sport.data;

import com.nraynaud.sport.Workout;

import java.util.List;

public class StatisticsData {
    public final PaginatedCollection<Workout> workouts;

    public final Double globalDistance;

    public final List<DisciplineDistance> distanceByDisciplines;

    public StatisticsData(final PaginatedCollection<Workout> workouts,
                          final Double globalDistance,
                          final List<DisciplineDistance> distanceByDisciplines) {
        this.workouts = workouts;
        this.globalDistance = globalDistance;
        this.distanceByDisciplines = distanceByDisciplines;
    }
}

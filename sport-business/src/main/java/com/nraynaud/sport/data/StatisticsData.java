package com.nraynaud.sport.data;

import com.nraynaud.sport.Workout;

import java.util.List;

public class StatisticsData<T> {
    public final PaginatedCollection<Workout> workouts;

    public final Double globalDistance;

    public final List<DisciplineData<T>> distanceByDisciplines;

    public StatisticsData(final PaginatedCollection<Workout> workouts,
                          final Double globalDistance,
                          final List<DisciplineData<T>> distanceByDisciplines) {
        this.workouts = workouts;
        this.globalDistance = globalDistance;
        this.distanceByDisciplines = distanceByDisciplines;
    }
}

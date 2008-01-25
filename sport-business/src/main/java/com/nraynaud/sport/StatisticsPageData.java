package com.nraynaud.sport;

import java.util.List;

public class StatisticsPageData {
    public final List<Workout> workouts;

    public final Double globalDistance;

    public final List<DisciplineDistance> distanceByDisciplines;

    public static class DisciplineDistance {
        public final String discipline;

        public final Double distance;

        public DisciplineDistance(final String discipline, final Double distance) {
            this.discipline = discipline;
            this.distance = distance;
        }
    }

    public final List<Message> messages;

    public StatisticsPageData(final List<Workout> workouts,
                              final Double globalDistance,
                              final List<DisciplineDistance> distanceByDisciplines,
                              final List<Message> messages) {
        this.workouts = workouts;
        this.globalDistance = globalDistance;
        this.distanceByDisciplines = distanceByDisciplines;
        this.messages = messages;
    }
}

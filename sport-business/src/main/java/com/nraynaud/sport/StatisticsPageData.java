package com.nraynaud.sport;

import java.util.List;

public interface StatisticsPageData {
    public List<Workout> getWorkouts();

    public Double getGlobalDistance();

    public List<DisciplineDistance> getDistanceByDisciplines();

    interface DisciplineDistance {
        String getDiscipline();

        Double getDistance();
    }

    public List<Message> getMessages();
}

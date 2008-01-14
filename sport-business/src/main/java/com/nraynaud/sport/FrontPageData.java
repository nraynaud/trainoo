package com.nraynaud.sport;

import java.util.List;
import java.util.Map;

public interface FrontPageData {
    public List<Workout> getWorkouts();
    public Double getGlobalDistance();
    public List<DisciplineDistance> getDistanceByDisciplines();

    interface DisciplineDistance {
        String getDiscipline();
        Double getDistance();
    }
}

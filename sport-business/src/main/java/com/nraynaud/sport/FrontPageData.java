package com.nraynaud.sport;

import java.util.List;
import java.util.Map;

public interface FrontPageData {
    public List<Workout> getWorkouts();
    public Double getGlobalDistance();
    public Map<String, Double> getDistanceByDisciplines();
}

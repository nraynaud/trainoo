package com.nraynaud.sport.importer;

import java.util.Date;

public interface WorkoutCollector {
    void collectWorkout(String nikePlusId, final String discipline, Date date, Double distance, Long duration);

    void endCollection();
}

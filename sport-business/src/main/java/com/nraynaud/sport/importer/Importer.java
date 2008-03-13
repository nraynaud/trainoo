package com.nraynaud.sport.importer;

public interface Importer {
    public void importWorkouts(String login, String password, WorkoutCollector collector) throws FailureException;
}

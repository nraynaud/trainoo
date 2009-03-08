package com.nraynaud.sport.importer;

import java.net.URL;

public interface Importer {
    public void importWorkouts(String login, String password, WorkoutCollector collector) throws FailureException;

    public String getId(String login, String password) throws FailureException;

    public byte[] getPNGImage(String userId, String workoutId, final URL logo);
}

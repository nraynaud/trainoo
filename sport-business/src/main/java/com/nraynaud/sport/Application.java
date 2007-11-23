package com.nraynaud.sport;

import java.util.Date;
import java.util.List;

public interface Application {
    Workout createWorkout(Date date, final User user, final Long duration);

    List<Workout> getWorkoutsForUser(final User user);

    User createUser(String login, String password) throws UserAlreadyExistsException;

    User authenticate(String login, String password);

    User find(long id);

    List<Workout> getWorkouts();
}

package com.nraynaud.sport;

import java.util.Date;
import java.util.List;

public interface Application extends UserStore {
    Workout createWorkout(Date date,
                          final User user,
                          final Long duration,
                          final Double distance,
                          final String discipline);

    List<Workout> getWorkoutsForUser(final User user, final int limit);

    User createUser(String login, String password) throws UserAlreadyExistsException;

    User authenticate(String login, String password);

    List<Workout> getWorkouts();

    Workout getWorkout(final Long id, final User user);

    void updateWorkout(final Workout workout,
                       final Date date,
                       final Long duration,
                       final Double distance,
                       final String discipline);
}

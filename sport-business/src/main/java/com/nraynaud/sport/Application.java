package com.nraynaud.sport;

import java.util.Date;
import java.util.List;

public interface Application extends UserStore {
    Workout createWorkout(Date date,
                          final User user,
                          final Long duration,
                          final Double distance,
                          final String discipline);

    User createUser(String login, String password) throws UserAlreadyExistsException;

    User authenticate(String login, String password);

    Workout fetchWorkout(final Long id, final User user);

    void deleteWorkout(final Long id, final User user) throws WorkoutNotFoundException;

    void updateWorkout(final Long id,
                       final User user,
                       final Date date,
                       final Long duration,
                       final Double distance,
                       final String discipline) throws WorkoutNotFoundException;

    StatisticsPageData fetchFrontPageData();

    StatisticsPageData fetchWorkoutPageData(final User user);

    Message createMessage(User sender, String receiverName, String content, Date date) throws UserNotFoundException;

    @SuppressWarnings({"unchecked"})
    List<Message> fetchMessages(User receiver);

    List<String> fechLoginBeginningBy(final String prefix);

    void updateBib(final User user, final String town, final String description, final String webSite);
}

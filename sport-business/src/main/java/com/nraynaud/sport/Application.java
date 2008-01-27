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

    Workout fetchWorkoutAndCheckUser(final Long id, final User user, final boolean willWrite);

    void deleteWorkout(final Long id, final User user) throws WorkoutNotFoundException;

    void updateWorkout(final Long id,
                       final User user,
                       final Date date,
                       final Long duration,
                       final Double distance,
                       final String discipline) throws WorkoutNotFoundException;

    StatisticsPageData fetchFrontPageData();

    StatisticsPageData fetchWorkoutPageData(final User user);

    Message createPrivateMessage(User sender,
                                 String receiverName,
                                 String content,
                                 Date date,
                                 final Long aboutWorkout) throws UserNotFoundException;

    @SuppressWarnings({"unchecked"})
    List<Message> fetchMessages(User receiver);

    List<String> fechLoginBeginningBy(final String prefix);

    void updateBib(final User user, final String town, final String description, final String webSite);

    BibPageData fetchBibPageData(final User currentUser, final Long targetUserId) throws UserNotFoundException;

    ConversationData fetchConvertationData(final User sender, final String receiver, final Long aboutWorkoutId);

    Message createPublicMessage(final User sender, final String content, final Date date, final Long aboutWorkoutId);
}

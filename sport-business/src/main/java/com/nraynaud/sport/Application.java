package com.nraynaud.sport;

import com.nraynaud.sport.data.*;

import java.util.Date;
import java.util.List;

public interface Application extends UserStore {
    Workout createWorkout(Date date,
                          final User user,
                          final Long duration,
                          final Double distance,
                          final String discipline);

    User createUser(String login, String password) throws UserAlreadyExistsException;

    User authenticate(String login, String password, final boolean rememberMe);

    Workout fetchWorkoutAndCheckUser(final Long id, final User user, final boolean willWrite) throws
            WorkoutNotFoundException;

    void deleteWorkout(final Long id, final User user) throws WorkoutNotFoundException;

    void updateWorkout(final Long id,
                       final User user,
                       final Date date,
                       final Long duration,
                       final Double distance,
                       final String discipline) throws WorkoutNotFoundException;

    StatisticsPageData fetchFrontPageData(final int firstIndex, final String discipline);

    UserPageData fetchUserPageData(final User user, final int firstIndex, final String discipline);

    PrivateMessage createPrivateMessage(User sender,
                                        String receiverName,
                                        String content,
                                        Date date,
                                        final Long aboutWorkout) throws UserNotFoundException, WorkoutNotFoundException;

    @SuppressWarnings({"unchecked"})
    List<PrivateMessage> fetchMessages(User receiver);

    List<String> fechLoginBeginningBy(final String prefix);

    void updateBib(final User user, final String town, final String description, final String webSite);

    BibPageData fetchBibPageData(final User currentUser, final Long targetUserId, final int workoutStartIndex) throws
            UserNotFoundException;

    ConversationData fetchConvertationData(final User sender, final String receiver, final Long aboutWorkoutId) throws
            WorkoutNotFoundException;

    PublicMessage createPublicMessage(final User sender,
                                      final String content,
                                      final Date date,
                                      final Long aboutWorkoutId) throws
            WorkoutNotFoundException;

    Workout fetchWorkout(Long id) throws WorkoutNotFoundException;

    WorkoutPageData fetchWorkoutPageData(final User currentUser, final Long workoutId, final int startIndex) throws
            WorkoutNotFoundException;

    boolean checkAndChangePassword(final User user, final String oldPassword, final String password);

    void forgetMe(final User user);

    List<NewMessageData> fetchNewMessagesCount(final User user);

    void deleteMessageFor(final Long id, final User user);

    void deletePublicMessageFor(final Long messageId, final User user);

    GroupPageData fetchGroupPageData(final User user, final Long groupId);

    void createGroup(final User user, final String name, final String description);

    void joinGroup(final User user, final Long groupId);

    void partGroup(final User user, final Long groupId);
}

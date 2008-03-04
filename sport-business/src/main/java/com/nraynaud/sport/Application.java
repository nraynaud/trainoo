package com.nraynaud.sport;

import com.nraynaud.sport.data.*;
import com.nraynaud.sport.mail.MailException;

import java.util.Date;
import java.util.List;

public interface Application extends UserStore {
    Workout createWorkout(Date date,
                          final User user,
                          final Long duration,
                          final Double distance,
                          final String discipline);

    User createUser(String login, String password, final String email) throws NameClashException, MailException;

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

    GlobalWorkoutsPageData fetchFrontPageData(final int firstIndex, final int pageSize, final String discipline);

    UserPageData fetchUserPageData(final User user, final int firstIndex, final String discipline);

    PrivateMessage createPrivateMessage(User sender,
                                        String receiverName,
                                        String content,
                                        Date date,
                                        final Long aboutWorkout) throws UserNotFoundException, WorkoutNotFoundException;

    List<String> fechLoginBeginningBy(final String prefix);

    void updateBib(final User user, final String town, final String description, final String webSite);

    BibPageData fetchBibPageData(final User currentUser, final Long targetUserId, final int workoutStartIndex,
                                 final int privateMessagesPageIndex) throws
            UserNotFoundException;

    ConversationData fetchConvertationData(final User sender, final String receiver, final Long aboutWorkoutId,
                                           final int startIndex) throws
            WorkoutNotFoundException;

    PublicMessage createPublicMessage(final User sender,
                                      final String content,
                                      final Date date,
                                      final Long topicId, final Topic.Kind topicKind) throws
            WorkoutNotFoundException;

    Workout fetchWorkout(Long id) throws WorkoutNotFoundException;

    WorkoutPageData fetchWorkoutPageData(final User currentUser, final Long workoutId, final int startIndex,
                                         final int messagesStartIndex, final int privateMessagesPageIndex) throws
            WorkoutNotFoundException;

    boolean checkAndChangePassword(final User user, final String oldPassword, final String password);

    void forgetMe(final User user);

    List<NewMessageData> fetchNewMessagesCount(final User user);

    void deleteMessageFor(final Long id, final User user);

    void deletePublicMessageFor(final Long messageId, final User user);

    GroupPageData fetchGroupPageData(final User user, final Long groupId, final int messageStartIndex,
                                     final int workoutStartIndex, final String discipline);

    Group createGroup(final User user, final String name, final String description) throws NameClashException;

    void joinGroup(final User user, final Long groupId);

    void partGroup(final User user, final Long groupId);

    Group fetchGroupForUpdate(final User user, final Long grouId) throws GroupNotFoundException, AccessDeniedException;

    void updateGroup(final User user, final Long groupId, final String name, final String description) throws
            GroupNotFoundException, AccessDeniedException, NameClashException;

    void updateMessage(final User user, final Long messageId, final String content, final MessageKind kind) throws
            AccessDeniedException;

    void setWorkoutParticipants(final User user, final Long workoutId, final String[] participants);

    User createUser(String login, String password) throws NameClashException;
}

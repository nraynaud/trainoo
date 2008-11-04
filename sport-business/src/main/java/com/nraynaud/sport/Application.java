package com.nraynaud.sport;

import com.nraynaud.sport.data.*;
import com.nraynaud.sport.mail.MailException;

import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface Application extends UserStore {

    User createUser(String login, String password, final String email) throws NameClashException, MailException;

    User authenticate(String login, String password, final boolean rememberMe);

    Workout fetchWorkoutForEdition(final Long id, final User user, final boolean willWrite) throws
            WorkoutNotFoundException, AccessDeniedException;

    void deleteWorkout(final Long id, final User user) throws WorkoutNotFoundException, AccessDeniedException;

    void updateWorkout(final Long id,
                       final User user,
                       final Date date,
                       final Long duration,
                       final Double distance,
                       final Long energy, final String discipline, final String comment) throws
            WorkoutNotFoundException,
            AccessDeniedException;

    GlobalWorkoutsPageData fetchFrontPageData(final int firstIndex, final int pageSize,
                                              final Collection<String> disciplines);

    /**
     * @param user
     * @param firstIndex
     * @param discipline selection, pass empty collection to retrieve everything without filtering
     * @return
     */
    UserPageData fetchUserPageData(final User user, final int firstIndex, final Collection<String> discipline);

    PrivateMessage createPrivateMessage(User sender,
                                        String receiverName,
                                        String content,
                                        Date date,
                                        final Long aboutWorkout) throws UserNotFoundException, WorkoutNotFoundException;

    List<String> fechLoginBeginningBy(final String prefix);

    List<User> fetchUsersBeginningByAndAddableToWorkout(final String prefix, final long id);

    void updateBib(final User user, final String town, final String description, final String webSite);

    /**
     * @param currentUser              the user LOOKING at the bib, can be null
     * @param targetUserId             the bib LOOKED AT
     * @param workoutStartIndex
     * @param privateMessagesPageIndex
     * @return
     * @throws UserNotFoundException
     */
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

    WorkoutPageData fetchWorkoutPageData(final User currentUser, final Long workoutId, final int similarPage,
                                         final int lastWorkoutsPage, final int messagesStartIndex,
                                         final int privateMessagesPageIndex) throws
            WorkoutNotFoundException;

    boolean checkAndChangePassword(final User user, final String oldPassword, final String password) throws
            MailException;

    void forgetMe(final User user);

    List<NewMessageData> fetchNewMessagesCount(final User user);

    int fetchTotalNewMessagesCount(final User user);

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

    void setWorkoutParticipants(final User user, final Long workoutId, final String[] participants) throws
            AccessDeniedException;

    void addWorkoutParticipants(final User user, final Long workoutId, final Long... participants) throws
            AccessDeniedException;

    void removeWorkoutParticipants(final User user, final Long workoutId, final Long... participants) throws
            AccessDeniedException;

    User createUser(String login, String password) throws NameClashException;

    void updateEmail(final User user, final String email);

    void updateNikePlusData(final User user, final String nikePlusEmail, final String nikePlusPassword,
                            final String nikePlusId);

    Workout createWorkout(Date date,
                          User user,
                          Long duration,
                          Double distance,
                          final Long energy, String discipline,
                          String comment,
                          String nikePlusId);

    /**
     * executes the runnable under only one transaction.
     * Usefull to do various action overt the application in one transaction
     *
     * @param runnable
     */
    void execute(final Runnable runnable);

    void forgotPassword(final String email) throws UserNotFoundException, MailException;

    void createTrack(final User user, final String title, final String points, final double length);

    List<Track> fetchTracks(final User user);

    List<Track> fetchTracks();

    Track fetchTrack(final Long id) throws TrackNotFoundException;

    void updateTrack(final User user, final long id, final String title, final String points) throws
            TrackNotFoundException,
            AccessDeniedException;

    void deleteTrack(final User user, final Long id) throws TrackNotFoundException, AccessDeniedException;

    void checkEditionGrant(final Workout workout, final User user) throws AccessDeniedException;

    StatisticsPageData fetchStatisticsPageData(final Long userId) throws UserNotFoundException;
}

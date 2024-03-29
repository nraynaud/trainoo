package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;
import com.nraynaud.sport.data.*;
import static com.nraynaud.sport.hibernate.SQLHelper.*;
import com.nraynaud.sport.mail.MailException;
import com.nraynaud.sport.mail.MailSender;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@SuppressWarnings({"unchecked"})
@Transactional(rollbackFor = {NameClashException.class, MailException.class})
public class HibernateApplication implements Application {

    private EntityManager entityManager;
    private static final Random TOKEN_GENERATOR = new Random();
    private WorkoutStore workoutStore;

    public Workout createWorkout(final Date date,
                                 final User user,
                                 final Long duration,
                                 final Double distance,
                                 final Long energy,
                                 final String discipline,
                                 final String debriefing,
                                 final String nikePlusId) {
        return workoutStore.createWorkout(date, user, duration, distance, energy, discipline, debriefing, nikePlusId);
    }

    /**
     * executes the runnable under only one transaction.
     * Usefull to do various action overt the application in one transaction
     *
     * @param runnable
     */
    public void execute(final Runnable runnable) {
        runnable.run();
    }

    public void forgotPassword(final String email) throws UserNotFoundException, MailException {
        final Query query = entityManager.createQuery("select u from UserImpl u where u.email=:cryptedMail");
        query.setParameter("cryptedMail", CipherHelper.cipher(email));
        final List<UserImpl> list = query.getResultList();
        if (list.isEmpty())
            throw new UserNotFoundException();
        for (final UserImpl user : list) {
            final String password = Helper.randomstring();
            user.setPassword(password);
            MailSender.forgotPasswordMail(user.getName(), password, email);
            entityManager.merge(user);
        }
    }

    public void createTrack(final User user, final String title, final String points, final double length) {
        final Query query = entityManager.createNativeQuery(
                "INSERT INTO TRACKS SET OWNER_ID=:userId, TITLE=:title, POINTS=:points, LENGTH=:length");
        query.setParameter("userId", user.getId());
        query.setParameter("title", title);
        query.setParameter("points", points);
        query.setParameter("length", length);
        query.executeUpdate();
    }

    public List<Track> fetchTracks(final User user) {
        final Query query = entityManager.createQuery("select t from TrackImpl t where t.user =:user");
        query.setParameter("user", user);
        return query.getResultList();
    }

    public User createUser(final String login, final String password, final String email) throws
            NameClashException, MailException {
        try {
            final UserImpl user = new UserImpl(login, password);
            if (email != null)
                user.setEmail(email);
            entityManager.persist(user);
            if (email != null)
                MailSender.sendSignupMail(login, password, email);
            return user;
        } catch (EntityExistsException e) {
            throw new NameClashException();
        }
    }

    /**
     * @return null if auth failed, user otherwise
     */
    public User authenticate(final String login, final String password, final boolean rememberMe) {
        final Query query = query("select u from UserImpl u where u.name=:user_login");
        query.setParameter("user_login", login);
        try {
            final UserImpl user = (UserImpl) query.getSingleResult();
            if (rememberMe && user.getRememberToken() == null) {
                final String token = generateToken();
                user.setRememberToken(token);
                entityManager.merge(user);
            }
            return user.checkPassword(password) ? user : null;
        } catch (NoResultException e) {
            return null;
        }
    }

    private static String generateToken() {
        final StringBuilder builder = new StringBuilder(260);
        while (builder.length() < 256) {
            builder.append(Long.toHexString(TOKEN_GENERATOR.nextLong()));
        }
        return builder.substring(0, 256);
    }

    public User fetchUser(final Long id) throws UserNotFoundException {
        final Query query = query("select u from UserImpl u where u.id=:id");
        query.setParameter("id", id);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        }
    }

    public User fetchRememberedUser(final String rememberCookie) throws UserNotFoundException {
        final Query query = query("select u from UserImpl u where u.rememberToken =:rememberCookie");
        query.setParameter("rememberCookie", rememberCookie);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        }
    }

    public User fetchUser(final String name) throws UserNotFoundException {
        final Query query = query("select u from UserImpl u where u.name=:name");
        query.setParameter("name", name);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        }
    }

    public Workout fetchWorkout(final Long id, final User user, final boolean willWrite) throws
            WorkoutNotFoundException, AccessDeniedException {
        return workoutStore.fetchWorkoutForEdition(id, user, willWrite);
    }

    public WorkoutPageData fetchWorkoutPageData(final User currentUser, final Long workoutId,
                                                final int similarPage, final int workoutStartIndex,
                                                final int messagesStartIndex,
                                                final int privateMessagesPageIndex) throws WorkoutNotFoundException {
        final Workout workout = workoutStore.fetchWorkout(workoutId);
        final PaginatedCollection<PrivateMessage> emptyPage = emptyPage();
        final PaginatedCollection<PrivateMessage> privateConversation = currentUser
                == null ? emptyPage : fetchPrivateConversation(currentUser, workout.getUser().getId(),
                privateMessagesPageIndex);
        return new WorkoutPageData(workout, fetchPublicMessages(Topic.Kind.WORKOUT, workoutId, 5, messagesStartIndex),
                getSimilarWorkouts(workout, similarPage),
                workoutStore.getWorkouts(workout.getUser(), EMPTY_STRING_LIST, workoutStartIndex, 10),
                privateConversation);
    }

    private PaginatedCollection<Workout> getSimilarWorkouts(final Workout workout, final int similarPageIndex) {
        return workoutStore.getSimilarWorkouts(workout, similarPageIndex);
    }

    private PaginatedCollection<PublicMessage> fetchPublicMessages(final Topic.Kind kind, final Long id,
                                                                   final int pageSize, final int pageIndex) {
        final Query query = query("select m from PublicMessageImpl m where m."
                + (kind == Topic.Kind.WORKOUT ? "workout" : "group")
                + ".id=:id order by m.date desc");
        query.setParameter("id", id);
        return paginateQuery(pageSize, pageIndex, query);
    }

    public boolean checkAndChangePassword(final User user, final String oldPassword, final String password) throws
            MailException {
        if (user.checkPassword(oldPassword)) {
            ((UserImpl) user).setPassword(password);
            entityManager.merge(user);
            final UserString email = user.getEmail();
            if (email != null)
                MailSender.sendPasswordChangeMail(user.getName().toString(), password, email.nonEscaped());
            return true;
        }
        return false;
    }

    public void forgetMe(final User user) {
        ((UserImpl) user).setRememberToken(null);
        entityManager.merge(user);
    }

    public List<NewMessageData> fetchNewMessagesCount(final User user) {
        final Query query = query(
                "select new com.nraynaud.sport.data.NewMessageData(m.sender.name, count(m)) from PrivateMessageImpl m where m.receiver = :user and m.read = false group by m.sender.name");
        query.setParameter("user", user);
        return query.getResultList();
    }

    public int fetchTotalNewMessagesCount(final User user) {
        int sum = 0;
        for (final NewMessageData newMessageData : fetchNewMessagesCount(user)) {
            sum += newMessageData.messageCount;
        }
        return sum;
    }

    public void deleteMessageFor(final Long id, final User user) {
        final Query query = entityManager.createNativeQuery(
                "update MESSAGES SET deleted_by=:user_id where ID=:id and deleted_by IS NULL and receiver_id <> sender_id and (receiver_id=:user_id or sender_id=:user_id)");
        query.setParameter("user_id", user.getId());
        query.setParameter("id", id);
        final int updated = query.executeUpdate();
        if (updated == 0) {
            final Query query2 = entityManager.createNativeQuery(
                    "delete from MESSAGES where ID=:id and (receiver_id=:user_id or sender_id=:user_id)");
            query2.setParameter("id", id);
            query2.setParameter("user_id", user.getId());
            query2.executeUpdate();
        }
    }

    public void deletePublicMessageFor(final Long messageId, final User user) {
        final Query query = entityManager.createNativeQuery(
                "delete from PUBLIC_MESSAGES where ID=:id and sender_id=:user_id");
        query.setParameter("user_id", user.getId());
        query.setParameter("id", messageId);
        query.executeUpdate();
    }

    public GroupPageData fetchGroupPageData(final User user, final Long groupId, final int messageStartIndex,
                                            final int workoutStartIndex, final String discipline) {
        final Collection<GroupData> result = fetchGroupDataForUser(user, false);
        final GroupImpl group;
        final WorkoutsData<DisciplineData.Count> workoutsData;
        final PaginatedCollection<User> users;
        final boolean member;
        if (groupId != null) {
            group = entityManager.find(GroupImpl.class, groupId);
            workoutsData = fetchStatisticsData(group, workoutStartIndex, discipline);
            users = fetchGroupMembers(group);
            member = user != null && isGroupMember(user, group);
        } else {
            workoutsData = null;
            group = null;
            users = emptyPage();
            member = false;
        }
        final PaginatedCollection<PublicMessage> messagePaginatedCollection = fetchPublicMessages(Topic.Kind.GROUP,
                groupId, 10, messageStartIndex);
        if (user != null && group != null) {
            updateLastGroupVisit(user, group);
        }
        return new GroupPageData(group, member, result, messagePaginatedCollection,
                workoutsData, users);
    }

    private boolean isGroupMember(final User user, final GroupImpl group) {
        final Query query = entityManager.createNativeQuery(
                "select 1 from GROUP_USER where USER_ID=:userId AND GROUP_ID=:groupId");
        query.setParameter("userId", user.getId());
        query.setParameter("groupId", group.getId());
        return query.getResultList().size() > 0;
    }

    private Collection<GroupData> fetchGroupDataForUser(final User user, final boolean restrictToSuscribed) {
        final String ifConnectedColumns = "max(ifnull(USER_ID=:userId, false))>0, sum(ifnull(USER_ID=:userId AND LAST_VISIT<PUBLIC_MESSAGES.`DATE`, false))";
        final Query query = entityManager.createNativeQuery(
                "select GROUPS.ID, name, count(DISTINCT GROUP_USER.USER_ID), "
                        + (user != null ? ifConnectedColumns : " 0,0")
                        + " from GROUPS left join  GROUP_USER on GROUP_ID=ID "
                        + " left join PUBLIC_MESSAGES on PUBLIC_MESSAGES.GROUP_ID=GROUP_USER.GROUP_ID "
                        + (restrictToSuscribed ? " where GROUP_USER.USER_ID=:userId" : "")
                        + " group by GROUPS.ID order by CREATION_DATE");
        if (user != null)
            query.setParameter("userId", user.getId());
        final List<Object[]> list = query.getResultList();
        final Collection<GroupData> result = new ArrayList<GroupData>(list.size());
        for (final Object[] o : list)
            result.add(new GroupData(((Number) o[0]).longValue(), UserStringImpl.valueOf(String.valueOf(o[1])),
                    ((Number) o[2]).longValue(), ((Number) o[3]).intValue() != 0, ((Number) o[4]).intValue()));
        return result;
    }

    private void updateLastGroupVisit(final User user, final GroupImpl group) {
        if (group != null) {
            final Query query = entityManager.createNativeQuery(
                    "update GROUP_USER SET LAST_VISIT=:now where GROUP_ID=:groupId and USER_ID=:userId");
            query.setParameter("now", new Date());
            query.setParameter("groupId", group.getId());
            query.setParameter("userId", user.getId());
            query.executeUpdate();
        }
    }

    private PaginatedCollection<User> fetchGroupMembers(final Group group) {
        final String queryString = "select u from GroupImpl g inner join g.members u where g=:group order by u.name";
        final Query query = query(queryString);
        query.setParameter("group", group);
        return paginateList(0, 100, query.getResultList());
    }

    private Query query(final String queryString) {
        return entityManager.createQuery(queryString);
    }

    public Group createGroup(final User user, final String name, final String description) throws NameClashException {
        final GroupImpl group = new GroupImpl(name, user, description, new Date());
        try {
            entityManager.persist(group);
            joinGroup(user, group.getId());
            return group;
        } catch (EntityExistsException e) {
            throw new NameClashException();
        }
    }

    public void joinGroup(final User user, final Long groupId) {
        final Query query = entityManager.createNativeQuery(
                "insert GROUP_USER SET GROUP_ID=:groupId, USER_ID=:userId, JOINING_DATE=:joiningDate, LAST_VISIT=:joiningDate");
        query.setParameter("groupId", groupId);
        query.setParameter("userId", user.getId());
        query.setParameter("joiningDate", new Date());
        query.executeUpdate();
    }

    public void partGroup(final User user, final Long groupId) {
        final Query query = entityManager.createNativeQuery(
                "DELETE FROM GROUP_USER WHERE GROUP_ID=:groupId AND USER_ID=:userId");
        query.setParameter("groupId", groupId);
        query.setParameter("userId", user.getId());
        query.executeUpdate();
    }

    public Group fetchGroupForUpdate(final User user, final Long groupId) throws
            GroupNotFoundException,
            AccessDeniedException {
        final GroupImpl group = entityManager.find(GroupImpl.class, groupId);
        if (group == null)
            throw new GroupNotFoundException();
        if (group.getOwner().equals(user))
            return group;
        throw new AccessDeniedException();
    }

    public void updateGroup(final User user, final Long groupId, final String name, final String description) throws
            GroupNotFoundException, AccessDeniedException, NameClashException {
        final GroupImpl group = entityManager.find(GroupImpl.class, groupId);
        if (group == null)
            throw new GroupNotFoundException();
        if (group.getOwner().equals(user)) {
            final Query query = entityManager.createNativeQuery(
                    "update GROUPS SET NAME=:name, DESCRIPTION=:description where ID=:groupId and OWNER_ID=:userId");
            query.setParameter("name", name);
            query.setParameter("description", description);
            query.setParameter("groupId", groupId);
            query.setParameter("userId", user.getId());
            try {
                query.executeUpdate();
            } catch (EntityExistsException e) {
                throw new NameClashException();
            }
        } else
            throw new AccessDeniedException();
    }

    public void updateMessage(final User user, final Long messageId, final String content,
                              final MessageKind kind) throws
            AccessDeniedException {
        final String table = kind == MessageKind.PRIVATE ? "MESSAGES" : "PUBLIC_MESSAGES";
        final Query query = entityManager.createNativeQuery(
                "update " + table + " SET CONTENT=:content where ID=:id and SENDER_ID=:userId");
        query.setParameter("content", content);
        query.setParameter("id", messageId);
        query.setParameter("userId", user.getId());
        if (query.executeUpdate() != 1)
            throw new AccessDeniedException();
    }

    public void setWorkoutParticipants(final User user, final Long workoutId, final String[] participants) throws
            AccessDeniedException {
        workoutStore.setWorkoutParticipants(user, workoutId, participants);
    }

    public void addWorkoutParticipants(final User user, final Long workoutId, final Long... participants) throws
            AccessDeniedException {
        final WorkoutImpl workout = entityManager.find(WorkoutImpl.class, workoutId);
        if (!workout.getUser().equals(user))
            throw new AccessDeniedException();
        final Set<Long> participantsSet = new HashSet<Long>(Arrays.asList(participants));
        final Query query = createParticipantsInsertUnionQuery(workoutId, participantsSet);
        query.executeUpdate();
    }

    public void removeWorkoutParticipants(final User user, final Long workoutId, final Long... participants) throws
            AccessDeniedException {
        final WorkoutImpl workout = entityManager.find(WorkoutImpl.class, workoutId);
        if (!workout.getUser().equals(user))
            throw new AccessDeniedException();
        final Set<Long> participantsWithoutSelf = new HashSet<Long>(Arrays.asList(participants));
        participantsWithoutSelf.remove(user.getId());
        final Query query = createParticipantsDeleteQuery(workoutId, participantsWithoutSelf);
        query.executeUpdate();
    }

    private Query createParticipantsInsertUnionQuery(final Long workoutId, final Set<Long> participants) {
        final SQLHelper.Predicate listPred = createInListPredicate("ID", participants, "participant");
        final Query query = entityManager.createNativeQuery(
                "insert INTO WORKOUT_USER (USER_ID, WORKOUT_ID) SELECT ID, :workoutId FROM USERS WHERE"
                        + " ID not in (select USER_ID from WORKOUT_USER where WORKOUT_ID = :workoutId) and "
                        + listPred);
        query.setParameter("workoutId", workoutId);
        listPred.bindVariables(query);
        return query;
    }

    private Query createParticipantsDeleteQuery(final Long workoutId, final Set<Long> participants) {
        final SQLHelper.Predicate idPred = createInListPredicate("USER_ID", participants, "participant");
        final Query query = entityManager.createNativeQuery(
                "delete FROM WORKOUT_USER WHERE WORKOUT_ID = :workoutId AND " + idPred);
        query.setParameter("workoutId", workoutId);
        idPred.bindVariables(query);
        return query;
    }

    public void updateWorkout(final Long id,
                              final User user,
                              final Date date,
                              final Long duration,
                              final Double distance,
                              final Long energy, final String discipline, final String debriefing,
                              final String trackId) throws
            WorkoutNotFoundException, AccessDeniedException, TrackNotFoundException {
        final TrackImpl track = trackId != null && !trackId.isEmpty() ? fetchTrackForUpdate(user,
                Long.parseLong(trackId)) : null;
        workoutStore.updateWorkout(id, user, date, duration, distance, energy, discipline, debriefing, track);
    }

    @SuppressWarnings({"unchecked"})
    public GlobalWorkoutsPageData fetchFrontPageData(final int firstIndex, final int pageSize,
                                                     final Collection<String> disciplines) {
        return new GlobalWorkoutsPageData(fetchRecentMessages(),
                fetchStatisticsData(null, disciplines, firstIndex, pageSize));
    }

    private PaginatedCollection<PublicMessage> fetchRecentMessages() {
        final Query query = query("select m from PublicMessageImpl m order by m.date desc");
        final int pageSize = 5;
        final int startIndex = 0;
        final List<PublicMessage> list = paginatedQuery(pageSize, startIndex, query);
        return paginateList(startIndex, pageSize, list);
    }

    @SuppressWarnings({"unchecked"})
    private <T> List<DisciplineData<T>> aggregateByDiscipline(final User user,
                                                              final DisciplineAggregator<T> aggregator) {
        return aggregate("user", user, aggregator, " from WorkoutImpl w where 1=1 "
                + (user != null ? " and  :user MEMBER OF w.participants" : "")
                + " group by w.discipline");
    }

    private <T> List<DisciplineData<T>> aggregateByDiscipline(final Group group,
                                                              final DisciplineAggregator<T> aggregator) {
        return aggregate("group", group, aggregator,
                " from GroupImpl g left join g.members u left join u.workouts w where"
                        + " g=:group group by w.discipline having w.discipline is not null");
    }

    private <T> List<DisciplineData<T>> aggregate(final String paramName, final Object paramValue,
                                                  final DisciplineAggregator<T> aggregator,
                                                  final String selectionPart) {
        final Query query = query("select " + aggregator.projectionPart + selectionPart);
        if (paramValue != null)
            query.setParameter(paramName, paramValue);
        return aggregator.castqueryResult(query.getResultList());
    }

    private Double fetchGlobalDistance(final Group group) {
        final Query query = query(
                "select sum(w.distance) from GroupImpl g left join g.members u left join u.workouts w where g=:group");
        query.setParameter("group", group);
        return (Double) query.getSingleResult();
    }

    private Double fetchGlobalDistance(final User user) {
        final Query query = query("select sum(w.distance) from WorkoutImpl w where 1=1"
                + (user != null ? " and :user MEMBER OF w.participants" : ""));
        if (user != null)
            query.setParameter("user", user);
        final Double result = (Double) query.getSingleResult();
        return result == null ? Double.valueOf(0) : result;
    }

    public UserPageData fetchUserPageData(final User user, final int firstIndex, final Collection<String> disciplines) {
        final Collection<ConversationSummary> correspondants = fetchCorrespondents(user);
        return new UserPageData(correspondants,
                fetchStatisticsData(user, disciplines, firstIndex, 10),
                fetchGroupDataForUser(user, true));
    }

    private WorkoutsData<DisciplineData.Count> fetchStatisticsData(final User user,
                                                                   final Collection<String> disciplines,
                                                                   final int firstIndex,
                                                                   final int pageSize) {
        final PaginatedCollection<Workout> workouts = workoutStore.getWorkouts(user, disciplines, firstIndex, pageSize);
        final Double globalDistance = fetchGlobalDistance(user);
        final List<DisciplineData<DisciplineData.Count>> disciplineData = aggregateByDiscipline(user,
                DisciplineAggregator.COUNT_AGGREGATOR);
        return new WorkoutsData<DisciplineData.Count>(workouts, globalDistance, disciplineData);
    }

    private WorkoutsData<DisciplineData.Count> fetchStatisticsData(final Group group, final int firstIndex,
                                                                   String discipline) {
        if (discipline != null && discipline.length() == 0)
            discipline = null;
        final PaginatedCollection<Workout> workouts = workoutStore.getWorkouts(group, discipline, firstIndex, 10);
        final Double globalDistance = fetchGlobalDistance(group);
        final List<DisciplineData<DisciplineData.Count>> disciplineData = aggregateByDiscipline(group,
                DisciplineAggregator.COUNT_AGGREGATOR);
        return new WorkoutsData<DisciplineData.Count>(workouts, globalDistance, disciplineData);
    }

    private Collection<ConversationSummary> fetchCorrespondents(final User user) {
        final Map<String, ConversationSummary> correspondants = new HashMap<String, ConversationSummary>();
        {
            final Query query = query(
                    "select m.sender.name, m.receiver.name, count(m), m.read from PrivateMessageImpl m where (m.receiver=:user OR "
                            + "(m.sender=:user)) AND(m.deleter IS NULL OR m.deleter <> :user) "
                            + "group by m.sender.name, m.receiver.name, m.read");
            query.setParameter("user", user);
            for (final Object[] row : (List<Object[]>) query.getResultList()) {
                final boolean sent = row[0].equals(((UserImpl) user).getBareName());
                final String name = (String) (sent ? row[1] : row[0]);
                final ConversationSummary previous = correspondants.get(name);
                final long count = ((Number) row[2]).longValue();
                final long newCount;
                if (row[3].equals(Boolean.FALSE) && !sent)
                    newCount = count;
                else
                    newCount = 0;
                if (previous != null) {
                    correspondants.put(name, new ConversationSummary(UserStringImpl.valueOf(name),
                            count + previous.messageCount, newCount + previous.newMessageCount));
                } else
                    correspondants.put(name, new ConversationSummary(UserStringImpl.valueOf(name), count, newCount));
            }
        }
        return new TreeSet<ConversationSummary>(correspondants.values());
    }

    public void deleteWorkout(final Long id, final User user) throws WorkoutNotFoundException, AccessDeniedException {
        final Workout workout = fetchWorkout(id, user, true);
        separatePrivateMessagesFromWorkout(id);
        deletePublicMessageAboutWorkout(id);
        entityManager.remove(workout);
    }

    private void deletePublicMessageAboutWorkout(final Long id) {
        final Query query = entityManager.createNativeQuery(
                "DELETE FROM PUBLIC_MESSAGES WHERE WORKOUT_ID=:id");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    private void separatePrivateMessagesFromWorkout(final Long id) {
        final Query query = entityManager.createNativeQuery(
                "UPDATE MESSAGES SET WORKOUT_ID=NULL WHERE WORKOUT_ID=:id AND RECEIVER_ID IS NOT NULL");
        query.setParameter("id", id);
        query.executeUpdate();
    }

    @PersistenceContext
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
        workoutStore = new WorkoutStore(entityManager);
    }

    public PrivateMessage createPrivateMessage(final User sender, final String receiverName, final String content,
                                               final Date date, final Long workoutId) throws
            UserNotFoundException, WorkoutNotFoundException {
        final User receiver = receiverName != null ? fetchUser(receiverName) : null;
        return createMessage(sender, receiver, content, date, workoutId);
    }

    private PrivateMessage createMessage(final User sender, final User receiver, final String content, final Date date,
                                         final Long workoutId) throws WorkoutNotFoundException {
        final Workout workout = workoutId != null ? workoutStore.fetchWorkout(workoutId) : null;
        final PrivateMessageImpl message = new PrivateMessageImpl(sender, receiver, date, content, workout);
        entityManager.persist(message);
        return message;
    }

    public PublicMessage createPublicMessage(final User sender, final String content, final Date date,
                                             final Long topicId, final Topic.Kind topicKind) throws
            WorkoutNotFoundException {
        final PublicMessageImpl message;
        if (topicKind == Topic.Kind.WORKOUT) {
            final Workout workout = workoutStore.fetchWorkout(topicId);
            message = new PublicMessageImpl(sender, date, content, workout);
        } else {
            final GroupImpl group = entityManager.find(GroupImpl.class, topicId);
            message = new PublicMessageImpl(sender, date, content, group);
        }
        entityManager.persist(message);
        return message;
    }

    @SuppressWarnings({"unchecked"})
    public List<String> fechLoginBeginningBy(final String prefix) {
        final Query query = query(
                "select u.name from UserImpl u where u.name<>'googlebot' AND u.name LIKE CONCAT(:prefix, '%')");
        query.setParameter("prefix", prefix);
        return query.getResultList();
    }

    public List<User> fetchUsersBeginningByAndAddableToWorkout(final String prefix, final long id) {
        final Query query = entityManager.createQuery(
                "select u from UserImpl u, WorkoutImpl workout where u.name<>'googlebot' AND u.name LIKE CONCAT(:prefix, '%')"
                        + " AND u not MEMBER OF workout.participants AND workout.id = :id ");
        query.setParameter("prefix", prefix);
        query.setParameter("id", id);
        return query.getResultList();
    }

    public void updateBib(final User user, final String town, final String description, final String webSite) {
        final UserImpl userImpl = (UserImpl) user;
        userImpl.setTown(town);
        userImpl.setDescription(description);
        userImpl.setWebSite(webSite);
        entityManager.merge(user);
    }

    public BibPageData fetchBibPageData(final User currentUser, final Long targetUserId,
                                        final int workoutStartIndex, final int privateMessagesPageIndex) throws
            UserNotFoundException {
        final User target;
        final PaginatedCollection<PrivateMessage> privateMessages;
        if (currentUser == null) {
            target = fetchUser(targetUserId);
            privateMessages = emptyPage();
        } else {
            target = currentUser.getId().equals(targetUserId) ? currentUser : fetchUser(targetUserId);
            privateMessages = fetchPrivateConversation(currentUser, targetUserId, privateMessagesPageIndex);
        }
        final PaginatedCollection<Workout> workouts = workoutStore.getWorkouts(target, EMPTY_STRING_LIST,
                workoutStartIndex, 10);
        return new BibPageData(target, privateMessages, workouts);
    }

    private PaginatedCollection<PrivateMessage> fetchPrivateConversation(final User currentUser,
                                                                         final Long targetUserId, final int pageIndex) {
        return fetchConversation("((m.receiver.id=:userId AND m.sender=:currentUser)"
                + "OR(m.receiver=:currentUser AND m.sender.id=:userId))"
                + "AND (deleter <> :currentUser OR deleter IS NULL)", 5, pageIndex, "currentUser", currentUser,
                "userId", targetUserId);
    }

    public PaginatedCollection<PrivateMessage> fetchConversation(final User currentUser, final String receiverName,
                                                                 final int startIndex) {
        return fetchConversation(
                "((m.receiver.name=:receiverName AND m.sender=:currentUser)"
                        + "OR(m.receiver=:currentUser AND m.sender.name=:receiverName)) "
                        + "AND (deleter <> :currentUser OR deleter IS NULL)", 5, startIndex, "currentUser", currentUser,
                "receiverName", receiverName);
    }

    public ConversationData fetchConvertationData(final User sender, final String receiver, final Long aboutWorkoutId,
                                                  final int startIndex) throws WorkoutNotFoundException {
        final Workout aboutWorkout = aboutWorkoutId == null ? null : workoutStore.fetchWorkout(aboutWorkoutId);
        final ConversationData conversationData = new ConversationData(fetchConversation(sender, receiver, startIndex),
                aboutWorkout, UserStringImpl.valueOf(receiver));
        for (final PrivateMessage privateMessage : conversationData.privateMessages) {
            final PrivateMessageImpl message1 = (PrivateMessageImpl) privateMessage;
            if (!message1.isRead() && message1.getReceiver().equals(sender)) {
                message1.setRead(true);
                message1.setNew(true);
                entityManager.merge(privateMessage);
            }
        }
        return conversationData;
    }

    private PaginatedCollection<PrivateMessage> fetchConversation(final String where, final int pageSize,
                                                                  final int startIndex, final Object... args) {
        final Query query = query("select m from PrivateMessageImpl m where (" + where + ") order by m.date desc");
        if (args.length % 2 != 0)
            throw new IllegalArgumentException(
                    "arg count should be even. \"argname1\",argvalue1, \"argname2\", argavalue2");
        for (int i = 0; i < args.length; i += 2)
            query.setParameter((String) args[i], args[i + 1]);
        return paginateQuery(pageSize, startIndex, query);
    }

    public User createUser(final String login, final String password) throws NameClashException {
        try {
            return createUser(login, password, null);
        } catch (MailException e) {
            //unexcpected
            throw new RuntimeException(e);
        }
    }

    public void updateEmail(final User user, final String email) {
        ((UserImpl) user).setEmail(email.length() > 0 ? email : null);
        entityManager.merge(user);
    }

    public void updateNikePlusData(final User user, final String nikePlusEmail, final String nikePlusPassword,
                                   final String nikePlusId) {
        final UserImpl userImpl = (UserImpl) user;
        userImpl.setNikePluEmail(nikePlusEmail);
        userImpl.setNikePlusPassword(nikePlusPassword);
        userImpl.setNikePlusId(nikePlusId);
        entityManager.merge(userImpl);
    }

    public List<Track> fetchTracks() {
        final Query query = entityManager.createQuery("select t from TrackImpl t");
        return query.getResultList();
    }

    public TrackImpl fetchTrack(final Long id) throws TrackNotFoundException {
        final TrackImpl track = entityManager.find(TrackImpl.class, id);
        if (track == null)
            throw new TrackNotFoundException();
        return track;
    }

    public void updateTrack(final User user, final long id, final String title, final String points) throws
            TrackNotFoundException, AccessDeniedException {
        final TrackImpl track = fetchTrackForUpdate(user, id);
        track.setTitle(title);
        track.setPoints(points);
        entityManager.merge(track);
    }

    private TrackImpl fetchTrackForUpdate(final User user, final long id) throws TrackNotFoundException,
            AccessDeniedException {
        final TrackImpl track = fetchTrack(id);
        if (!track.getUser().equals(user))
            throw new AccessDeniedException();
        return track;
    }

    public void deleteTrack(final User user, final Long id) throws TrackNotFoundException, AccessDeniedException {
        entityManager.remove(fetchTrackForUpdate(user, id.longValue()));
    }

    public void checkEditionGrant(final Workout workout, final User user) throws AccessDeniedException {
        workoutStore.checkEditionGrant(workout, user);
    }

    private static void bindQuery(final Query query, final User user, final String discipline) {
        query.setParameter("user", user);
        if (discipline != null)
            query.setParameter("discipline", discipline);
    }

    public StatisticsPageData fetchStatisticsPageData(final Long userId, final String discipline) throws
            UserNotFoundException {
        final User user = fetchUser(userId);
        final String fromWhereClause = " from WorkoutImpl w where :user MEMBER OF w.participants "
                + (discipline != null ? " and discipline = :discipline " : " ");
        final Query disciplineQuery = query("select w.discipline, count(*) from WorkoutImpl w "
                + "WHERE :user MEMBER OF w.participants GROUP BY w.discipline ORDER BY count(*) DESC");
        disciplineQuery.setParameter("user", user);
        final Query query = query("select sum(w.distance) " + fromWhereClause);
        bindQuery(query, user, discipline);
        final Query query2 = query(
                "select year(w.date), sum(w.distance), sum(w.duration), sum(w.energy) "
                        + fromWhereClause
                        + " GROUP BY year(w.date) ORDER BY year(w.date) DESC");
        bindQuery(query2, user, discipline);
        final Query query3 = query(
                "select year(w.date), month(w.date), sum(w.distance), sum(w.duration), sum(w.energy) "
                        + fromWhereClause + "GROUP BY year(w.date), month(w.date) "
                        + "ORDER BY year(w.date)  DESC, month(w.date)  DESC");
        bindQuery(query3, user, discipline);
        return new StatisticsPageData(user, disciplineQuery.getResultList(), query.getSingleResult(),
                query2.getResultList(),
                query3.getResultList());
    }

    public SitemapData fetchSitemapData() {
        final Query userQuery = query("select u.id from UserImpl u where u.name<>'googlebot'");
        final Query workoutQuery = query("select w.id from WorkoutImpl w");
        final Query groupQuery = query("select g.id from GroupImpl g");
        return new SitemapData(userQuery.getResultList(), workoutQuery.getResultList(), groupQuery.getResultList());
    }

    public Workout fetchWorkout(final Long id) {
        return workoutStore.fetchWorkout(id);
    }

    public User createFacebookUser(final String login, final Long facebookId) throws NameClashException {
        try {
            final UserImpl user = new UserImpl(login, facebookId);
            entityManager.persist(user);
            return user;
        } catch (EntityExistsException e) {
            throw new NameClashException();
        }
    }

    public void facebookConnect(final User user, final Long facebookId) throws NameClashException {
        final UserImpl user1 = (UserImpl) user;
        try {
            user1.setFacebookId(facebookId);
            entityManager.merge(user);
            entityManager.flush();
        } catch (EntityExistsException e) {
            user1.setFacebookId(null);
            throw new NameClashException();
        }
    }

    public User facebookLogin(final Long facebookId) {
        final Query query = query("select u from UserImpl u where u.facebookId=:facebookId");
        query.setParameter("facebookId", facebookId);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

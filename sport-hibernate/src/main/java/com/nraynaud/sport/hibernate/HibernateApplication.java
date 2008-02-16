package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;
import com.nraynaud.sport.data.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@SuppressWarnings({"unchecked"})
@Transactional(rollbackFor = NameClashException.class)
public class HibernateApplication implements Application {

    private EntityManager entityManager;
    private static final Random TOKEN_GENERATOR = new Random();

    public Workout createWorkout(final Date date,
                                 final User user,
                                 final Long duration,
                                 final Double distance,
                                 final String discipline) {
        final Workout workout = new WorkoutImpl(user, date, duration, distance, discipline);
        entityManager.persist(workout);
        return workout;
    }

    @SuppressWarnings({"unchecked"})
    private PaginatedCollection<Workout> getWorkouts(final User user, final String discipline, final int startIndex,
                                                     final int pageSize) {
        return fetchWorkouts(user, discipline, startIndex, pageSize, false);
    }

    private PaginatedCollection<Workout> fetchWorkouts(final User user, final String discipline, final int startIndex,
                                                       final int pageSize,
                                                       final boolean lastpage) {
        final Query query = query("select w, count(m) from WorkoutImpl w left join w.publicMessages m where 1=1"
                + (user != null ? " and w.user =:user" : "")
                + (discipline != null ? " and w.discipline =:discipline" : "")
                + " group by w.id, w.user, w.date, w.duration, w.distance, w.discipline order by  w.date desc");
        if (user != null)
            query.setParameter("user", user);
        if (discipline != null)
            query.setParameter("discipline", discipline);
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        final List<Object[]> result = query.getResultList();
        // we went too far, get back one page.
        if (result.isEmpty() && startIndex != 0)
            return fetchWorkouts(user, discipline, startIndex - pageSize, pageSize, true);
        final List<Workout> list = new ArrayList(result.size());
        for (final Object[] row : result) {
            final WorkoutImpl workout = (WorkoutImpl) row[0];
            workout.setMessageNumber(((Number) row[1]).longValue());
            list.add(workout);
        }
        return paginateList(startIndex, pageSize, lastpage, list);
    }

    private PaginatedCollection<Workout> getWorkouts(final Group group, final String discipline, final int firstIndex,
                                                     final int pageSize, final boolean lastpage) {
        final Query query = query("select w, count(m) "
                + "from GroupImpl g inner join g.members u inner join u.workouts w left join w.publicMessages m "
                + "where g =:group"
                + (discipline != null ? " and w.discipline =:discipline" : "")
                + " group by w.id, w.user, w.date, w.duration, w.distance, w.discipline order by  w.date desc");
        query.setParameter("group", group);
        if (discipline != null)
            query.setParameter("discipline", discipline);
        query.setFirstResult(firstIndex);
        query.setMaxResults(pageSize);
        final List<Object[]> result = query.getResultList();
        // we went too far, get back one page.
        if (result.isEmpty() && firstIndex != 0)
            return getWorkouts(group, discipline, firstIndex - pageSize, pageSize, true);
        final List<Workout> list = new ArrayList(result.size());
        for (final Object[] row : result) {
            final WorkoutImpl workout = (WorkoutImpl) row[0];
            workout.setMessageNumber(((Number) row[1]).longValue());
            list.add(workout);
        }
        return paginateList(firstIndex, pageSize, lastpage, list);
    }

    private static <T> PaginatedCollection<T> paginateList(final int startIndex, final int pageSize,
                                                           final boolean lastpage, final List<T> list) {
        return new PaginatedCollection<T>() {
            public boolean hasPrevious() {
                return !lastpage && list.size() >= pageSize;
            }

            public boolean hasNext() {
                return startIndex > 0;
            }

            public int getPreviousIndex() {
                return startIndex + pageSize;
            }

            public int getNextIndex() {
                return startIndex - pageSize;
            }

            public boolean isEmpty() {
                return list.isEmpty();
            }

            public Iterator<T> iterator() {
                return list.iterator();
            }
        };
    }

    @Transactional(rollbackFor = NameClashException.class)
    public User createUser(final String login, final String password) throws NameClashException {
        try {
            final User user = new UserImpl(login, password);
            entityManager.persist(user);
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

    public Workout fetchWorkoutAndCheckUser(final Long id, final User user, final boolean willWrite) throws
            WorkoutNotFoundException {
        final Workout workout = fetchWorkout(id);
        if (workout == null || !willWrite || workout.getUser().equals(user))
            return workout;
        else
            return null;
    }

    public Workout fetchWorkout(final Long id) throws WorkoutNotFoundException {
        return entityManager.find(WorkoutImpl.class, id);
    }

    public WorkoutPageData fetchWorkoutPageData(final User currentUser, final Long workoutId,
                                                final int workoutStartIndex, final int messagesStartIndex,
                                                final int privateMessagesPageIndex) throws WorkoutNotFoundException {
        final Workout workout = fetchWorkout(workoutId);
        final PaginatedCollection<PrivateMessage> emptyPage = emptyPage();
        final PaginatedCollection<PrivateMessage> privateConversation = currentUser
                == null ? emptyPage : fetchPrivateConversation(currentUser, workout.getUser().getId(),
                privateMessagesPageIndex);
        return new WorkoutPageData(workout, fetchPublicMessages(Topic.Kind.WORKOUT, workoutId, 5, messagesStartIndex),
                getWorkouts(workout.getUser(), null, workoutStartIndex, 10), privateConversation);
    }

    private PaginatedCollection<PublicMessage> fetchPublicMessages(final Topic.Kind kind, final Long id,
                                                                   final int pageSize, final int pageIndex) {
        final Query query = query("select m from PublicMessageImpl m where m."
                + (kind == Topic.Kind.WORKOUT ? "workout" : "group")
                + ".id=:id order by m.date desc");
        query.setParameter("id", id);
        return paginateQuery(pageSize, pageIndex, query);
    }

    public boolean checkAndChangePassword(final User user, final String oldPassword, final String password) {
        if (user.checkPassword(oldPassword)) {
            ((UserImpl) user).setPassword(password);
            entityManager.merge(user);
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
        final StatisticsData statisticsData;
        final PaginatedCollection<User> users;
        if (groupId != null) {
            group = entityManager.find(GroupImpl.class, groupId);
            statisticsData = fetchStatisticsData(group, workoutStartIndex, discipline);
            users = fetchGroupMembers(group);
        } else {
            statisticsData = null;
            group = null;
            users = emptyPage();
        }
        final PaginatedCollection<PublicMessage> messagePaginatedCollection = fetchPublicMessages(Topic.Kind.GROUP,
                groupId, 10, messageStartIndex);
        if (user != null && group != null)
            updateLastGroupVisit(user, group);
        return new GroupPageData(group, result, messagePaginatedCollection,
                statisticsData, users);
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

    private static <T> PaginatedCollection<T> emptyPage() {
        return paginateList(0, 1, true, Collections.<T>emptyList());
    }

    private PaginatedCollection<User> fetchGroupMembers(final Group group) {
        final String queryString = "select u from GroupImpl g inner join g.members u where g=:group";
        final Query query = query(queryString);
        query.setParameter("group", group);
        return paginateList(0, 100, true, query.getResultList());
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
                              final Message.Kind kind) throws
            AccessDeniedException {
        final String table = kind == Message.Kind.PRIVATE ? "MESSAGES" : "PUBLIC_MESSAGES";
        final Query query = entityManager.createNativeQuery(
                "update " + table + " SET CONTENT=:content where ID=:id and SENDER_ID=:userId");
        query.setParameter("content", content);
        query.setParameter("id", messageId);
        query.setParameter("userId", user.getId());
        if (query.executeUpdate() != 1)
            throw new AccessDeniedException();
    }

    public void updateWorkout(final Long id,
                              final User user,
                              final Date date,
                              final Long duration,
                              final Double distance,
                              final String discipline) throws WorkoutNotFoundException {
        final WorkoutImpl workoutImpl = (WorkoutImpl) fetchWorkoutAndCheckUser(id, user, true);
        workoutImpl.setDate(date);
        workoutImpl.setDuration(duration);
        workoutImpl.setDistance(distance);
        workoutImpl.setDiscipline(discipline);
        entityManager.merge(workoutImpl);
    }

    @SuppressWarnings({"unchecked"})
    public GlobalWorkoutsPageData fetchFrontPageData(final int firstIndex, final String discipline) {
        return new GlobalWorkoutsPageData(fetchRecentMessages(),
                fetchStatisticsData((User) null, firstIndex, discipline));
    }

    private PaginatedCollection<PublicMessage> fetchRecentMessages() {
        final Query query = query("select m from PublicMessageImpl m order by m.date desc");
        query.setMaxResults(5);
        return paginateList(0, 5, true, query.getResultList());
    }

    @SuppressWarnings({"unchecked"})
    private List<DisciplineDistance> fetchDistanceByDiscipline(final User user) {
        final String string =
                "select new com.nraynaud.sport.data.DisciplineDistance(w.discipline, sum(w.distance))"
                        + " from WorkoutImpl w where w.distance is not null"
                        + (user != null ? " and w.user = :user" : "")
                        + " group by w.discipline";
        final Query nativeQuery = query(string);
        if (user != null)
            nativeQuery.setParameter("user", user);
        return (List<DisciplineDistance>) nativeQuery.getResultList();
    }

    private List<DisciplineDistance> fetchDistanceByDiscipline(final Group group) {
        final String string =
                "select new com.nraynaud.sport.data.DisciplineDistance("
                        + "w.discipline, sum(w.distance))"
                        + " from GroupImpl g left join g.members u left join u.workouts w where w.distance is not null"
                        + " and g=:group group by w.discipline";
        final Query query = query(string);
        query.setParameter("group", group);
        return (List<DisciplineDistance>) query.getResultList();
    }

    private Double fetchGlobalDistance(final Group group) {
        final Query query = query(
                "select sum(w.distance) from GroupImpl g left join g.members u left join u.workouts w where g=:group");
        query.setParameter("group", group);
        return (Double) query.getSingleResult();
    }

    private Double fetchGlobalDistance(final User user) {
        final Query query = query("select sum(w.distance) from WorkoutImpl w where 1=1"
                + (user != null ? " and w.user=:user" : ""));
        if (user != null)
            query.setParameter("user", user);
        final Double result = (Double) query.getSingleResult();
        return result == null ? Double.valueOf(0) : result;
    }

    public UserPageData fetchUserPageData(final User user, final int firstIndex, final String discipline) {
        final Collection<ConversationSummary> correspondants = fetchCorrespondents(user);
        return new UserPageData(correspondants, fetchStatisticsData(user, firstIndex, discipline),
                fetchGroupDataForUser(user, true));
    }

    private StatisticsData fetchStatisticsData(final User user, final int firstIndex, String discipline) {
        if (discipline != null && discipline.length() == 0)
            discipline = null;
        final PaginatedCollection<Workout> workouts = getWorkouts(user, discipline, firstIndex, 10);
        final Double globalDistance = fetchGlobalDistance(user);
        final List<DisciplineDistance> distanceByDiscpline = fetchDistanceByDiscipline(user);
        return new StatisticsData(workouts, globalDistance, distanceByDiscpline);
    }

    private StatisticsData fetchStatisticsData(final Group group, final int firstIndex, String discipline) {
        if (discipline != null && discipline.length() == 0)
            discipline = null;
        final PaginatedCollection<Workout> workouts = getWorkouts(group, discipline, firstIndex, 10, false);
        final Double globalDistance = fetchGlobalDistance(group);
        final List<DisciplineDistance> distanceByDiscpline = fetchDistanceByDiscipline(group);
        return new StatisticsData(workouts, globalDistance, distanceByDiscpline);
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

    public void deleteWorkout(final Long id, final User user) throws WorkoutNotFoundException {
        final Workout workout = fetchWorkoutAndCheckUser(id, user, true);
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
    }

    public PrivateMessage createPrivateMessage(final User sender, final String receiverName, final String content,
                                               final Date date, final Long workoutId) throws
            UserNotFoundException, WorkoutNotFoundException {
        final User receiver = receiverName != null ? fetchUser(receiverName) : null;
        return createMessage(sender, receiver, content, date, workoutId);
    }

    private PrivateMessage createMessage(final User sender, final User receiver, final String content, final Date date,
                                         final Long workoutId) throws WorkoutNotFoundException {
        final Workout workout = workoutId != null ? fetchWorkout(workoutId) : null;
        final PrivateMessageImpl message = new PrivateMessageImpl(sender, receiver, date, content, workout);
        entityManager.persist(message);
        return message;
    }

    public PublicMessage createPublicMessage(final User sender, final String content, final Date date,
                                             final Long topicId, final Topic.Kind topicKind) throws
            WorkoutNotFoundException {
        final PublicMessageImpl message;
        if (topicKind == Topic.Kind.WORKOUT) {
            final Workout workout = fetchWorkout(topicId);
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
        final PaginatedCollection<Workout> workouts = getWorkouts(target, null, workoutStartIndex, 10);
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
        final Workout aboutWorkout = aboutWorkoutId == null ? null : fetchWorkout(aboutWorkoutId);
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

    @SuppressWarnings({"unchecked"})
    public PaginatedCollection<PrivateMessage> fetchPublicMessagesForWorkout(final Long workoutId) {
        return fetchConversation("m.workout.id =:workoutId AND m.sender is null", 5, 0, "workoutId", workoutId);
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

    private static <T> PaginatedCollection<T> paginateQuery(final int pageSize, final int startIndex,
                                                            final Query query) {
        query.setMaxResults(pageSize);
        query.setFirstResult(startIndex);
        final List list = query.getResultList();
        return paginateList(startIndex, pageSize, false, list);
    }
}

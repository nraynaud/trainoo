package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;
import com.nraynaud.sport.data.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.*;

@SuppressWarnings({"unchecked"})
@Transactional
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
        final Query query = entityManager.createQuery(
                "select w, count(m) from WorkoutImpl w left join w.publicMessages m where 1=1"
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
        return new PaginatedCollection<Workout>() {
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

            public Iterator<Workout> iterator() {
                return list.iterator();
            }
        };
    }

    @Transactional(rollbackFor = UserAlreadyExistsException.class)
    public User createUser(final String login, final String password) throws UserAlreadyExistsException {
        try {
            final User user = new UserImpl(login, password);
            entityManager.persist(user);
            return user;
        } catch (EntityExistsException e) {
            throw new UserAlreadyExistsException();
        }
    }

    /**
     * @return null if auth failed, user otherwise
     */
    public User authenticate(final String login, final String password, final boolean rememberMe) {
        final Query query = entityManager.createQuery("select u from UserImpl u where u.name=:user_login");
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
        final Query query = entityManager.createQuery("select u from UserImpl u where u.id=:id");
        query.setParameter("id", id);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        }
    }

    public User fetchRememberedUser(final String rememberCookie) throws UserNotFoundException {
        final Query query = entityManager.createQuery(
                "select u from UserImpl u where u.rememberToken =:rememberCookie");
        query.setParameter("rememberCookie", rememberCookie);
        try {
            return (User) query.getSingleResult();
        } catch (NoResultException e) {
            throw new UserNotFoundException();
        }
    }

    public User fetchUser(final String name) throws UserNotFoundException {
        final Query query = entityManager.createQuery("select u from UserImpl u where u.name=:name");
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
        if (workout == null || !willWrite || workout.getUser().getId().equals(user.getId()))
            return workout;
        else
            return null;
    }

    public Workout fetchWorkout(final Long id) throws WorkoutNotFoundException {
        return entityManager.find(WorkoutImpl.class, id);
    }

    public WorkoutPageData fetchWorkoutPageData(final User currentUser, final Long workoutId,
                                                final int startIndex) throws
            WorkoutNotFoundException {
        final PaginatedCollection<PrivateMessage> emptyPage = new PaginatedCollection<PrivateMessage>() {

            public boolean hasPrevious() {
                return false;
            }

            public boolean hasNext() {
                return false;
            }

            public int getPreviousIndex() {
                return 0;
            }

            public int getNextIndex() {
                return 0;
            }

            public boolean isEmpty() {
                return true;
            }

            public Iterator<PrivateMessage> iterator() {
                return new Iterator<PrivateMessage>() {

                    public boolean hasNext() {
                        return false;
                    }

                    public PrivateMessage next() {
                        throw new NoSuchElementException();
                    }

                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
        final Workout workout = fetchWorkout(workoutId);
        final PaginatedCollection<PrivateMessage> privateConversation = currentUser
                == null ? emptyPage : fetchPrivateConversation(currentUser, workout.getUser().getId());
        return new WorkoutPageData(workout, fetchPublicMessages(Topic.Kind.WORKOUT, workoutId),
                getWorkouts(workout.getUser(), null, startIndex, 10), privateConversation);
    }

    private PaginatedCollection<PublicMessage> fetchPublicMessages(final Topic.Kind kind, final Long id) {
        final Query query = entityManager.createQuery(
                "select m from PublicMessageImpl m where m."
                        + (kind == Topic.Kind.WORKOUT ? "workout" : "group")
                        + ".id=:id order by m.date desc");
        query.setParameter("id", id);
        final List list = query.getResultList();
        return new PaginatedCollection<PublicMessage>() {
            public boolean hasPrevious() {
                return false;
            }

            public boolean hasNext() {
                return false;
            }

            public int getPreviousIndex() {
                return 0;
            }

            public int getNextIndex() {
                return 0;
            }

            public boolean isEmpty() {
                return list.isEmpty();
            }

            public Iterator<PublicMessage> iterator() {
                return list.iterator();
            }
        };
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
        final Query query = entityManager.createQuery(
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

    public GroupPageData fetchGroupPageData(final User user, final Long groupId) {
        final Query query = entityManager.createNativeQuery(
                "select GROUPS.ID, name, count(USER_ID), "
                        + (user != null ? "max(ifnull(USER_ID=:userId, false))>0" : "0")
                        + " from GROUPS left join  GROUP_USER on GROUP_ID=ID group by GROUPS.ID order by CREATION_DATE");
        if (user != null)
            query.setParameter("userId", user.getId());
        final List<Object[]> list = query.getResultList();
        final Collection<GroupData> result = new ArrayList<GroupData>(list.size());
        for (final Object[] o : list)
            result.add(new GroupData(((Number) o[0]).longValue(), String.valueOf(o[1]), ((Number) o[2]).longValue(),
                    ((Number) o[3]).intValue() != 0));
        final GroupImpl group;
        if (groupId != null) {
            group = entityManager.find(GroupImpl.class, groupId);
        } else
            group = null;
        return new GroupPageData(group, result, fetchPublicMessages(Topic.Kind.GROUP, groupId));
    }

    public void createGroup(final User user, final String name, final String description) {
        final GroupImpl group = new GroupImpl(name, user, description, new Date());
        entityManager.persist(group);
        joinGroup(user, group.getId());
    }

    public void joinGroup(final User user, final Long groupId) {
        final Query query = entityManager.createNativeQuery(
                "insert GROUP_USER SET GROUP_ID=:groupId, USER_ID=:userId, JOINING_DATE=:joiningDate");
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
    public StatisticsPageData fetchFrontPageData(final int firstIndex, final String discipline) {
        return fetchUserPageData(null, firstIndex, discipline);
    }

    @SuppressWarnings({"unchecked"})
    private List<StatisticsPageData.DisciplineDistance> fetchDistanceByDiscipline(final User user) {
        final String string =
                "select new com.nraynaud.sport.data.StatisticsPageData$DisciplineDistance(w.discipline, sum(w.distance))"
                        + " from WorkoutImpl w where w.distance is not null"
                        + (user != null ? " and w.user = :user" : "")
                        + " group by w.discipline";
        final Query nativeQuery = entityManager.createQuery(string);
        if (user != null)
            nativeQuery.setParameter("user", user);
        return (List<StatisticsPageData.DisciplineDistance>) nativeQuery.getResultList();
    }

    private Double fetchGlobalDistance(final User user, final String discipline) {
        final String string = "select sum(w.distance) from WorkoutImpl w where 1=1";
        final Query query = entityManager.createQuery(string
                + (user != null ? " and w.user=:user" : "")
                + (discipline != null ? " and w.discipline=:discipline" : ""));
        if (user != null)
            query.setParameter("user", user);
        if (discipline != null)
            query.setParameter("discipline", discipline);
        return (Double) query.getSingleResult();
    }

    public UserPageData fetchUserPageData(final User user, final int firstIndex, final String discipline) {
        final PaginatedCollection<Workout> workouts = getWorkouts(user, discipline, firstIndex, 10);
        final Double globalDistance = fetchGlobalDistance(user, discipline);
        final List<StatisticsPageData.DisciplineDistance> distanceByDiscpline = fetchDistanceByDiscipline(user);
        final Collection<ConversationSumary> correspondants = fetchCorrespondents(user);
        return new UserPageData(workouts, globalDistance, distanceByDiscpline, correspondants);
    }

    private Collection<ConversationSumary> fetchCorrespondents(final User user) {
        final Map<String, ConversationSumary> correspondants = new HashMap<String, ConversationSumary>();
        {
            final Query query = entityManager.createQuery(
                    "select m.sender.name, m.receiver.name, count(m), m.read from PrivateMessageImpl m where (m.receiver=:user OR "
                            + "(m.sender=:user)) AND(m.deleter IS NULL OR m.deleter <> :user) "
                            + "group by m.sender.name, m.receiver.name, m.read");
            query.setParameter("user", user);
            for (final Object[] row : (List<Object[]>) query.getResultList()) {
                final boolean sent = row[0].equals(user.getName());
                final String name = (String) (sent ? row[1] : row[0]);
                final ConversationSumary previous = correspondants.get(name);
                final long count = ((Number) row[2]).longValue();
                final long newCount;
                if (row[3].equals(Boolean.FALSE) && !sent)
                    newCount = count;
                else
                    newCount = 0;
                if (previous != null) {
                    correspondants.put(name, new ConversationSumary(name,
                            count + previous.messageCount, newCount + previous.newMessageCount));
                } else
                    correspondants.put(name, new ConversationSumary(name, count, newCount));
            }
        }
        return new TreeSet<ConversationSumary>(correspondants.values());
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
        final Query query = entityManager.createQuery(
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
                                        final int workoutStartIndex) throws UserNotFoundException {
        final User target = currentUser.getId().equals(targetUserId) ? currentUser : fetchUser(targetUserId);
        final PaginatedCollection<PrivateMessage> privateMessages = fetchPrivateConversation(currentUser, targetUserId);
        final PaginatedCollection<Workout> workouts = getWorkouts(target, null, workoutStartIndex, 10);
        return new BibPageData(target, privateMessages, workouts);
    }

    private PaginatedCollection<PrivateMessage> fetchPrivateConversation(final User currentUser,
                                                                         final Long targetUserId) {
        return fetchConversation("((m.receiver.id=:userId AND m.sender=:currentUser)"
                + "OR(m.receiver=:currentUser AND m.sender.id=:userId))"
                + "AND (deleter <> :currentUser OR deleter IS NULL)",
                "currentUser",
                currentUser,
                "userId",
                targetUserId);
    }

    public PaginatedCollection<PrivateMessage> fetchConversation(final User currentUser, final String receiverName) {
        return fetchConversation(
                "((m.receiver.name=:receiverName AND m.sender=:currentUser)"
                        + "OR(m.receiver=:currentUser AND m.sender.name=:receiverName)) "
                        + "AND (deleter <> :currentUser OR deleter IS NULL)",
                "currentUser",
                currentUser,
                "receiverName",
                receiverName);
    }

    public ConversationData fetchConvertationData(final User sender,
                                                  final String receiver,
                                                  final Long aboutWorkoutId) throws
            WorkoutNotFoundException {
        final Workout aboutWorkout = aboutWorkoutId == null ? null : fetchWorkout(aboutWorkoutId);
        final ConversationData conversationData = new ConversationData(fetchConversation(sender, receiver),
                aboutWorkout);
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
        return fetchConversation("m.workout.id =:workoutId AND m.sender is null", "workoutId", workoutId);
    }

    private PaginatedCollection<PrivateMessage> fetchConversation(final String where, final Object... args) {
        final Query query = entityManager.createQuery(
                "select m from PrivateMessageImpl m where (" + where + ") order by m.date desc");
        if (args.length % 2 != 0)
            throw new IllegalArgumentException(
                    "arg count should be even. \"argname1\",argvalue1, \"argname2\", argavalue2");
        for (int i = 0; i < args.length; i += 2)
            query.setParameter((String) args[i], args[i + 1]);
        final List list = query.getResultList();
        return new PaginatedCollection<PrivateMessage>() {
            public boolean hasPrevious() {
                return false;
            }

            public boolean hasNext() {
                return false;
            }

            public int getPreviousIndex() {
                return 0;
            }

            public int getNextIndex() {
                return 0;
            }

            public boolean isEmpty() {
                return list.isEmpty();
            }

            public Iterator<PrivateMessage> iterator() {
                return list.iterator();
            }
        };
    }
}

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
    private PaginatedCollection<Workout> getWorkouts(final User user, final int startIndex, final int pageSize) {
        return fetchWorkouts(user, startIndex, pageSize, false);
    }

    private PaginatedCollection<Workout> fetchWorkouts(final User user, final int startIndex, final int pageSize,
                                                       final boolean lastpage) {
        final Query query = entityManager.createQuery(
                "select w, count(m) from WorkoutImpl w left join w.messages m where m.receiver is null "
                        + (user != null ? "AND w.user =:user" : "")
                        + " group by w.id, w.user, w.date, w.duration, w.distance, w.discipline order by  w.date desc");
        if (user != null)
            query.setParameter("user", user);
        query.setFirstResult(startIndex);
        query.setMaxResults(pageSize);
        final List<Object[]> result = query.getResultList();
        // we went too far, get back one page.
        if (result.isEmpty() && startIndex != 0)
            return fetchWorkouts(user, startIndex - pageSize, pageSize, true);
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
        final Workout workout = fetchWorkout(workoutId);
        final List<Message> privateConversation = (List<Message>) (currentUser == null ? Collections.emptyList() :
                fetchPrivateConversation(currentUser, workout.getUser().getId()));
        return new WorkoutPageData(workout,
                fetchConversation("receiver IS NULL AND workout.id=:workoutId", "workoutId", workoutId),
                getWorkouts(workout.getUser(), startIndex, 10), privateConversation);
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
                "select new com.nraynaud.sport.data.NewMessageData(m.sender.name, count(m)) from MessageImpl m where m.receiver = :user and m.read = false group by m.sender.name");
        query.setParameter("user", user);
        return query.getResultList();
    }

    public void deleteMessageFor(final Long id, final User user) {
        final Query query = entityManager.createNativeQuery(
                "update MESSAGES SET deleted_by=:user_id where ID=:id and deleted_by IS NULL and receiver_id <> sender_id");
        query.setParameter("user_id", user.getId());
        query.setParameter("id", id);
        final int updated = query.executeUpdate();
        if (updated == 0) {
            final Query query2 = entityManager.createNativeQuery(
                    "delete from MESSAGES where ID=:id");
            query2.setParameter("id", id);
            query2.executeUpdate();
        }
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
    public StatisticsPageData fetchFrontPageData(final int firstIndex) {
        return fetchUserPageData(null, firstIndex);
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

    private Double fetchGlobalDistance(final User user) {
        final String string = "select sum(w.distance) from WorkoutImpl w";
        final Query query = entityManager.createQuery(string + (user != null ? " where w.user=:user" : ""));
        if (user != null)
            query.setParameter("user", user);
        return (Double) query.getSingleResult();
    }

    public UserPageData fetchUserPageData(final User user, final int firstIndex) {
        final PaginatedCollection<Workout> workouts = getWorkouts(user, firstIndex, 10);
        final Double globalDistance = fetchGlobalDistance(user);
        final List<StatisticsPageData.DisciplineDistance> distanceByDiscpline = fetchDistanceByDiscipline(user);
        final Collection<ConversationSumary> correspondants = fetchCorrespondents(user);
        return new UserPageData(workouts, globalDistance, distanceByDiscpline, correspondants);
    }

    private Collection<ConversationSumary> fetchCorrespondents(final User user) {
        final Map<String, ConversationSumary> correspondants = new HashMap<String, ConversationSumary>();
        {
            final Query query = entityManager.createQuery(
                    "select m.sender.name, m.receiver.name, count(m), m.read from MessageImpl m where (m.receiver=:user OR "
                            + "(m.sender=:user AND m.receiver IS NOT NULL)) AND(m.deleter IS NULL OR m.deleter <> :user) "
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
                "DELETE FROM MESSAGES WHERE WORKOUT_ID=:id AND RECEIVER_ID IS NULL");
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

    public Message createPrivateMessage(final User sender, final String receiverName, final String content,
                                        final Date date, final Long workoutId) throws
            UserNotFoundException, WorkoutNotFoundException {
        final User receiver = receiverName != null ? fetchUser(receiverName) : null;
        return createMessage(sender, receiver, content, date, workoutId);
    }

    private Message createMessage(final User sender, final User receiver, final String content, final Date date,
                                  final Long workoutId) throws WorkoutNotFoundException {
        final Workout workout = workoutId != null ? fetchWorkout(workoutId) : null;
        final MessageImpl message = new MessageImpl(sender, receiver, date, content, workout);
        entityManager.persist(message);
        return message;
    }

    public Message createPublicMessage(final User sender, final String content, final Date date,
                                       final Long aboutWorkoutId) throws WorkoutNotFoundException {
        return createMessage(sender, null, content, date, aboutWorkoutId);
    }

    @SuppressWarnings({"unchecked"})
    public List<Message> fetchMessages(final User user) {
        return fetchConversation("m.receiver=:user OR (m.sender=:user AND m.receiver IS NOT NULL)", "user", user);
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
        final List<Message> messages = fetchPrivateConversation(currentUser, targetUserId);
        final PaginatedCollection<Workout> workouts = getWorkouts(target, workoutStartIndex, 10);
        return new BibPageData(target, messages, workouts);
    }

    private List<Message> fetchPrivateConversation(final User currentUser, final Long targetUserId) {
        return fetchConversation("((m.receiver.id=:userId AND m.sender=:currentUser)"
                + "OR(m.receiver=:currentUser AND m.sender.id=:userId))"
                + "AND (deleter <> :currentUser OR deleter IS NULL)",
                "currentUser",
                currentUser,
                "userId",
                targetUserId);
    }

    public List<Message> fetchConversation(final User currentUser, final String receiverName) {
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
        for (final Message message : conversationData.messages) {
            final MessageImpl message1 = (MessageImpl) message;
            if (!message1.isRead() && message1.getReceiver().equals(sender)) {
                message1.setRead(true);
                message1.setNew(true);
                entityManager.merge(message);
            }
        }
        return conversationData;
    }

    @SuppressWarnings({"unchecked"})
    public List<Message> fetchPublicMessagesForWorkout(final Long workoutId) {
        return fetchConversation("m.workout.id =:workoutId AND m.sender is null", "workoutId", workoutId);
    }

    private List<Message> fetchConversation(final String where, final Object... args) {
        final Query query = entityManager.createQuery(
                "select m from MessageImpl m where (" + where + ") order by m.date desc");
        if (args.length % 2 != 0)
            throw new IllegalArgumentException(
                    "arg count should be even. \"argname1\",argvalue1, \"argname2\", argavalue2");
        for (int i = 0; i < args.length; i += 2)
            query.setParameter((String) args[i], args[i + 1]);
        return query.getResultList();
    }
}

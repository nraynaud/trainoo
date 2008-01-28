package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;
import com.nraynaud.sport.data.BibPageData;
import com.nraynaud.sport.data.ConversationData;
import com.nraynaud.sport.data.StatisticsPageData;
import com.nraynaud.sport.data.WorkoutPageData;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked"})
@Transactional
public class HibernateApplication implements Application {

    private EntityManager entityManager;

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
    private List<Workout> getWorkouts(final User user, final int limit) {
        final String string = "select w from WorkoutImpl w "
                + (user != null ? "where w.user =:user" : "")
                + " order by  w.date desc";
        final Query query = entityManager.createQuery(
                string);
        if (user != null)
            query.setParameter("user", user);
        query.setMaxResults(limit);
        return query.getResultList();
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
    public User authenticate(final String login, final String password) {
        final Query query = entityManager.createQuery("select u from UserImpl u where u.name=:user_login");
        query.setParameter("user_login", login);
        try {
            final User user = (User) query.getSingleResult();
            return user.checkPassword(password) ? user : null;
        } catch (NoResultException e) {
            return null;
        }
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

    public WorkoutPageData fetchWorkoutPageData(final Long workoutId) throws WorkoutNotFoundException {
        final Workout workout = fetchWorkout(workoutId);
        return new WorkoutPageData(workout,
                fetchConversation("receiver IS NULL AND workout.id=:workoutId", "workoutId", workoutId),
                getWorkouts(workout.getUser(), 10));
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
    public StatisticsPageData fetchFrontPageData() {
        return fetchUserPageData(null);
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

    public StatisticsPageData fetchUserPageData(final User user) {
        final List<Workout> workouts = getWorkouts(user, 10);
        final Double globalDistance = fetchGlobalDistance(user);
        final List<StatisticsPageData.DisciplineDistance> distanceByDiscpline = fetchDistanceByDiscipline(user);
        final List<Message> messages = fetchMessages(user);
        return new StatisticsPageData(workouts, globalDistance, distanceByDiscpline, messages);
    }

    public void deleteWorkout(final Long id, final User user) throws WorkoutNotFoundException {
        final Workout workout = fetchWorkoutAndCheckUser(id, user, true);
        final Query query = entityManager.createNativeQuery("UPDATE MESSAGES SET WORKOUT_ID=NULL WHERE WORKOUT_ID=:id");
        query.setParameter("id", id);
        query.executeUpdate();
        entityManager.remove(workout);
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
                "select u.name from UserImpl u where u.name LIKE CONCAT(:prefix, '%')");
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

    public BibPageData fetchBibPageData(final User currentUser, final Long targetUserId) throws UserNotFoundException {
        final User target = currentUser.getId().equals(targetUserId) ? currentUser : fetchUser(targetUserId);
        final List<Message> messages = fetchConversation("(m.receiver.id=:userId AND m.sender=:currentUser)"
                + "OR(m.receiver=:currentUser AND m.sender.id=:userId)",
                "currentUser",
                currentUser,
                "userId",
                targetUserId);
        return new BibPageData(target, messages);
    }

    public List<Message> fetchConversation(final User currentUser, final String receiverName) {
        return fetchConversation(
                "(m.receiver.name=:receiverName AND m.sender=:currentUser)"
                        + "OR(m.receiver=:currentUser AND m.sender.name=:receiverName)",
                "currentUser",
                currentUser,
                "receiverName",
                receiverName);
    }

    public ConversationData fetchConvertationData(final User sender,
                                                  final String receiver,
                                                  final Long aboutWorkoutId) throws
            WorkoutNotFoundException {
        return new ConversationData(fetchConversation(sender, receiver), fetchWorkout(aboutWorkoutId));
    }

    @SuppressWarnings({"unchecked"})
    public List<Message> fetchPublicMessagesForWorkout(final Long workoutId) {
        return fetchConversation("m.workout.id =:workoutId AND m.sender is null", "workoutId", workoutId);
    }

    public List<Message> fetchConversation(final String where, final Object... args) {
        final Query query = entityManager.createQuery(
                "select m from MessageImpl m where (" + where + ") order by m.date asc");
        if (args.length % 2 != 0)
            throw new IllegalArgumentException(
                    "arg count should be even. \"argname1\",argvalue1, \"argname2\", argavalue2");
        for (int i = 0; i < args.length; i += 2)
            query.setParameter((String) args[i], args[i + 1]);
        return query.getResultList();
    }
}

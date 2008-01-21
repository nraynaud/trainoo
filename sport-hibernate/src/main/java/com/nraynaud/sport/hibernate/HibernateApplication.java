package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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

    public User fetchUser(final Long id) {
        final Query query = entityManager.createQuery("select u from UserImpl u where u.id=:id");
        query.setParameter("id", id);
        return (User) query.getSingleResult();
    }

    public Workout fetchWorkout(final Long id, final User user) {
        final WorkoutImpl workout = entityManager.find(WorkoutImpl.class, id);
        if (workout == null)
            return null;
        if (workout.getUser().getId().equals(user.getId()))
            return workout;
        else
            return null;
    }

    public void updateWorkout(final Long id,
                              final User user,
                              final Date date,
                              final Long duration,
                              final Double distance,
                              final String discipline) throws WorkoutNotFoundException {
        final WorkoutImpl workoutImpl = (WorkoutImpl) fetchWorkout(id, user);
        if (workoutImpl == null)
            throw new WorkoutNotFoundException();
        workoutImpl.setDate(date);
        workoutImpl.setDuration(duration);
        workoutImpl.setDistance(distance);
        workoutImpl.setDiscipline(discipline);
        entityManager.merge(workoutImpl);
    }

    @SuppressWarnings({"unchecked"})
    public StatisticsPageData fetchFrontPageData() {
        return fetchWorkoutPageData(null);
    }

    @SuppressWarnings({"unchecked"})
    private List<StatisticsPageData.DisciplineDistance> fetchDistanceByDiscipline(final User user) {
        final String string =
                "select new com.nraynaud.sport.hibernate.DisciplineDistanceImpl(w.discipline, sum(w.distance))"
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

    public StatisticsPageData fetchWorkoutPageData(final User user) {
        final List<Workout> workouts = getWorkouts(user, 10);
        final Double globalDistance = fetchGlobalDistance(user);
        final List<StatisticsPageData.DisciplineDistance> distanceByDiscpline = fetchDistanceByDiscipline(user);
        return statistics(workouts, globalDistance, distanceByDiscpline);
    }

    public void deleteWorkout(final Long id, final User user) throws WorkoutNotFoundException {
        final Workout workout = fetchWorkout(id, user);
        if (workout == null)
            throw new WorkoutNotFoundException();
        entityManager.remove(workout);
    }

    @PersistenceContext
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static StatisticsPageData statistics(final List<Workout> workouts,
                                                 final Double globalDistance,
                                                 final List<StatisticsPageData.DisciplineDistance> distanceByDiscpline) {
        return new StatisticsPageData() {
            public List<Workout> getWorkouts() {
                return workouts;
            }

            public Double getGlobalDistance() {
                return globalDistance;
            }

            public List<DisciplineDistance> getDistanceByDisciplines() {
                return distanceByDiscpline;
            }
        };
    }

    public Message createMessage(final User sender, final Long receiverId, final String content, final Date date) {
        final User receiver = fetchUser(receiverId);
        final MessageImpl message = new MessageImpl(sender, receiver, date, content);
        entityManager.persist(message);
        return message;
    }

    @SuppressWarnings({"unchecked"})
    public List<Message> fetchMessages(final User receiver) {
        final Query query = entityManager.createQuery("select m from MessageImpl m where m.receiver=:receiver");
        query.setParameter("receiver", receiver);
        return query.getResultList();
    }

    @SuppressWarnings({"unchecked"})
    public List<String> fechLoginBeginningBy(final String prefix) {
        final Query query = entityManager.createQuery(
                "select u.name from UserImpl u where u.name LIKE CONCAT(:prefix, '%')");
        query.setParameter("prefix", prefix);
        return query.getResultList();
    }
}

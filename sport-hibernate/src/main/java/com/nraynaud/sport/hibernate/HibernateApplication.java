package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Transactional
@SqlResultSetMapping(name = "distanceByDiscipline",
        entities = {@EntityResult(entityClass = DisciplineDistanceImpl.class)})
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
    private List<Workout> getWorkoutsForUser(final User user, final int limit) {
        final Query query = entityManager.createQuery(
                "select w from WorkoutImpl w where w.user =:user order by  w.date desc");
        query.setParameter("user", user);
        query.setMaxResults(limit);
        return query.getResultList();
    }

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
        } catch (EntityNotFoundException e) {
            return null;
        } catch (NoResultException e) {
            return null;
        }
    }

    public User getUser(final long id) {
        final Query query = entityManager.createQuery("select u from UserImpl u where u.id=:id");
        query.setParameter("id", Long.valueOf(id));
        return (User) query.getSingleResult();
    }

    public Workout getWorkout(final Long id, final User user) {
        final WorkoutImpl workout = entityManager.find(WorkoutImpl.class, id);
        if (workout == null)
            return null;
        if (workout.getUser().getId() == user.getId())
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
        final WorkoutImpl workoutImpl = (WorkoutImpl) getWorkout(id, user);
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
        final List<Workout> workouts = fetchworkouts();
        final Double globalDistance = fetchGlobalDistance(null);
        final List<StatisticsPageData.DisciplineDistance> distanceByDiscpline = fetchDistanceByDiscipline(null);
        return new MyStatisticsPageData(workouts, globalDistance, distanceByDiscpline);
    }

    @SuppressWarnings({"unchecked"})
    private List<StatisticsPageData.DisciplineDistance> fetchDistanceByDiscipline(final User user) {
        final String string = "select DISCIPLINE, sum(DISTANCE) as DISTANCE from WORKOUTS "
                + (user != null ? " where USER_ID = :user" : "")
                + " group by DISCIPLINE";
        final Query nativeQuery = entityManager.createNativeQuery(string, DisciplineDistanceImpl.class);
        if (user != null)
            nativeQuery.setParameter("user", Long.valueOf(user.getId()));
        return (List<StatisticsPageData.DisciplineDistance>) nativeQuery.getResultList();
    }

    private Double fetchGlobalDistance(final User user) {
        final String string = "select sum(w.distance) from WorkoutImpl w";
        final Query query = entityManager.createQuery(string + (user != null ? " where w.user=:user" : ""));
        if (user != null)
            query.setParameter("user", user);
        return (Double) query.getSingleResult();
    }

    @SuppressWarnings({"unchecked"})
    private List<Workout> fetchworkouts() {
        final Query query1 = entityManager.createQuery("select w from WorkoutImpl w order by w.date desc");
        query1.setMaxResults(15);
        return (List<Workout>) query1.getResultList();
    }

    public StatisticsPageData fetchWorkoutPageData(final User user) {
        final List<Workout> workouts = getWorkoutsForUser(user, 10);
        final Double globalDistance = fetchGlobalDistance(user);
        final List<StatisticsPageData.DisciplineDistance> distanceByDiscpline = fetchDistanceByDiscipline(user);
        return new MyStatisticsPageData(workouts, globalDistance, distanceByDiscpline);
    }

    public void deleteWorkout(final Long id, final User user) throws WorkoutNotFoundException {
        final Workout workout = getWorkout(id, user);
        if (workout == null)
            throw new WorkoutNotFoundException();
        entityManager.remove(workout);
    }

    @PersistenceContext
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    private static class MyStatisticsPageData implements StatisticsPageData {
        private final List<Workout> workouts;
        private final Double globalDistance;
        private final List<DisciplineDistance> distanceByDiscpline;

        public MyStatisticsPageData(final List<Workout> workouts,
                                    final Double globalDistance,
                                    final List<DisciplineDistance> distanceByDiscpline) {
            this.workouts = workouts;
            this.globalDistance = globalDistance;
            this.distanceByDiscpline = distanceByDiscpline;
        }

        public List<Workout> getWorkouts() {
            return workouts;
        }

        public Double getGlobalDistance() {
            return globalDistance;
        }

        public List<DisciplineDistance> getDistanceByDisciplines() {
            return distanceByDiscpline;
        }
    }
}

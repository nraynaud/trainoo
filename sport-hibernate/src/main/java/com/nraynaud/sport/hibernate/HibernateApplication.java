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
    public FrontPageData fetchFrontPageData() {
        final Query query = entityManager.createQuery("select sum(w.distance) from WorkoutImpl w");
        final Double globalDistance = (Double) query.getSingleResult();
        final Query query1 = entityManager.createQuery("select w from WorkoutImpl w order by w.date desc");
        query1.setMaxResults(15);
        final List<Workout> workouts = query1.getResultList();
        return new FrontPageData() {

            public List<Workout> getWorkouts() {
                return workouts;
            }

            public Double getGlobalDistance() {
                return globalDistance;
            }
        };
    }

    public WorkoutPageData fetchWorkoutPageData(final User user) {
        final List<Workout> workouts = getWorkoutsForUser(user, 10);
        return new WorkoutPageData() {
            public List<Workout> getWorkouts() {
                return workouts;
            }
        };
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
}

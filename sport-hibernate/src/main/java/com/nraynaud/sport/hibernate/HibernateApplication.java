package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserAlreadyExistsException;
import com.nraynaud.sport.Workout;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Transactional
public class HibernateApplication implements Application {

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Workout createWorkout(final Date date, final User user, final Long duration) {
        final Workout workout = new WorkoutImpl(user, date, duration, null);
        entityManager.persist(workout);
        return workout;
    }

    @SuppressWarnings({"unchecked"})
    public List<Workout> getWorkoutsForUser(final User user) {
        final Query query = entityManager.createQuery(
                "select w from WorkoutImpl w where w.user =:user order by  w.date asc");
        query.setParameter("user", user);
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

    public User find(final long id) {
        final Query query = entityManager.createQuery("select u from UserImpl u where u.id=:id");
        query.setParameter("id", Long.valueOf(id));
        return (User) query.getSingleResult();
    }

    @SuppressWarnings({"unchecked"})
    public List<Workout> getWorkouts() {
        final Query query = entityManager.createQuery("select w from WorkoutImpl w order by w.date desc");
        query.setMaxResults(15);
        return query.getResultList();
    }
}

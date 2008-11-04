package com.nraynaud.sport.hibernate;

import com.nraynaud.sport.*;
import com.nraynaud.sport.mail.MailException;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ApplicationTest {
    private HibernateApplication application;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        final EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("testPU");
        entityManager = managerFactory.createEntityManager();
        application = new HibernateApplication();
        application.setEntityManager(entityManager);
        entityManager.getTransaction().begin();
    }

    @After
    public void tearDown() {
        final EntityTransaction transaction = entityManager.getTransaction();
        if (transaction.getRollbackOnly())
            transaction.rollback();
        else
            transaction.commit();
    }

    @Test
    public void testUserFetching() throws NameClashException, UserNotFoundException {
        try {
            application.fetchUser(Long.valueOf(344444));
            fail("should not find user");
        } catch (UserNotFoundException e) {
            //ok
        }
        try {
            application.fetchUser("inexistant");
            fail("should not find user");
        } catch (UserNotFoundException e) {
            //ok
        }
        final User user = application.createUser("lol", "pouet");
        final User user1 = application.fetchUser(user.getId());
        assertEquals(user, user1);
        final User user2 = application.fetchUser("lol");
        assertEquals(user, user2);
    }

    @Test
    public void testRemeberToken() throws NameClashException, UserNotFoundException {
        final User user = application.createUser("lol", "pouet");
        final User user1 = application.authenticate("lol", "pouet", true);
        final User user2 = application.fetchRememberedUser(user1.getRememberToken());
        assertEquals(user, user2);
        application.forgetMe(user);
        assertNull(user.getRememberToken());
        try {
            application.fetchRememberedUser(user1.getRememberToken());
            fail("user should be forgotten");
        } catch (UserNotFoundException e) {
            //ok user not found
        }
    }

    @Test
    public void testUserCreation() throws NameClashException {
        assertNull(application.authenticate("lolé", "pass+é", false));
        application.createUser("lolé", "pass+é");
    }

    @Test
    public void testWorkoutCreation() throws NameClashException {
        assertNull(application.authenticate("lolé", "pass+é", false));
        final User user = application.createUser("lolé", "pass+é");
        final Workout workout = application.createWorkout(new Date(), user, new Long(12), new Double(10),
                null, "lol", null, null);
        final List<String> disciplines = Collections.emptyList();
        assertEquals(Arrays.asList(workout).iterator().next(),
                application.fetchFrontPageData(0, 20, disciplines).workoutsData.workouts.iterator().next());
    }

    @Test
    public void testUserAuth() throws NameClashException {
        assertNull(application.authenticate("lolé", "pass+é", false));
        application.createUser("lolé", "pass+é");
        {
            final User user = application.authenticate("lolé", "pass+é", false);
            assertNotNull(user);
            assertNull(user.getRememberToken());
        }
        {
            final User user = application.authenticate("lolé", "pass+é", true);
            assertNotNull(user.getRememberToken());
        }
        assertNull(application.authenticate("lolé", "wrongpass", false));
        assertNull(application.authenticate("jeanLouis", "wrongpass", false));
    }

    @Test
    public void testChangePassword() throws NameClashException, MailException {
        final User user = application.createUser("lolé", "pass+é");
        application.checkAndChangePassword(user, "pass+é", "newpass");
        assertNull(application.authenticate("lolé", "pass+é", false));
        assertEquals(user, application.authenticate("lolé", "newpass", false));
    }

    @Test
    public void testUserSameName() throws NameClashException {
        application.createUser("lolé", "pass+é");
        try {
            application.createUser("lolé", "pass+é");
            fail("two users with same name where created");
        } catch (NameClashException e) {
            //great !
        }
    }

    @Test
    public void testWorkoutFetching() throws NameClashException, WorkoutNotFoundException, AccessDeniedException {
        final User user = application.createUser("user", "lol");
        final Workout workout = application.createWorkout(new Date(), user, new Long(12), new Double(10),
                null, "lol", null, null);
        {
            final Workout workout1 = application.fetchWorkoutForEdition(workout.getId(), user, true);
            assertEquals(workout, workout1);
        }
        {
            try {
                application.fetchWorkoutForEdition(Long.valueOf(1043345), user, true);
                fail();
            } catch (WorkoutNotFoundException e) {
                //ok
            }
        }
        {
            final User user1 = application.createUser("user1", "lol");
            try {
                application.fetchWorkoutForEdition(workout.getId(), user1, true);
                fail();
            } catch (AccessDeniedException e) {
                //ok
            }
        }
    }

    @Test
    public void testFetchUser() throws NameClashException, UserNotFoundException {
        final User user = application.createUser("sender", "lol");
        final User user1 = application.fetchUser(user.getName().toString());
        assertEquals(user, user1);
        try {
            application.fetchUser("unknown");
            fail();
        } catch (UserNotFoundException e) {
        }
    }
}

import com.nraynaud.sport.*;
import com.nraynaud.sport.hibernate.HibernateApplication;
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
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
    public void testUserFetching() throws UserAlreadyExistsException {
        final User user = application.createUser("lol", "pouet");
        final User user1 = application.fetchUser(user.getId());
        assertEquals(user, user1);
    }

    @Test
    public void testUserCreation() throws UserAlreadyExistsException {
        assertNull(application.authenticate("lolé", "pass+é"));
        application.createUser("lolé", "pass+é");
    }


    @Test
    public void testWorkoutCreation() throws UserAlreadyExistsException {
        assertNull(application.authenticate("lolé", "pass+é"));
        final User user = application.createUser("lolé", "pass+é");
        final Workout workout = application.createWorkout(new Date(), user, new Long(12), new Double(10),
                "lol");
        assertEquals(Arrays.asList(workout), application.fetchFrontPageData().getWorkouts());
    }

    @Test
    public void testUserAuth() throws UserAlreadyExistsException {
        assertNull(application.authenticate("lolé", "pass+é"));
        application.createUser("lolé", "pass+é");
        assertNotNull(application.authenticate("lolé", "pass+é"));
        assertNull(application.authenticate("lolé", "wrongpass"));
        assertNull(application.authenticate("jeanLouis", "wrongpass"));
    }

    @Test
    public void testUserSameName() throws UserAlreadyExistsException {
        application.createUser("lolé", "pass+é");
        try {
            application.createUser("lolé", "pass+é");
            fail("two users with same name where created");
        } catch (UserAlreadyExistsException e) {
            //great !
        }
    }

    @Test
    public void testWorkoutFetching() throws UserAlreadyExistsException {
        final User user = application.createUser("user", "lol");
        final Workout workout = application.createWorkout(new Date(), user, new Long(12), new Double(10),
                "lol");
        {
            final Workout workout1 = application.fetchWorkout(workout.getId(), user);
            assertEquals(workout, workout1);
        }
        {
            final Workout workout2 = application.fetchWorkout(Long.valueOf(1043345), user);
            assertNull(workout2);
        }
        {
            final User user1 = application.createUser("user1", "lol");
            final Workout workout3 = application.fetchWorkout(workout.getId(), user1);
            assertNull(workout3);
        }

    }

    @Test
    public void testSendMessage() throws UserAlreadyExistsException, UserNotFoundException {
        final User sender = application.createUser("sender", "lol");
        final User receiver = application.createUser("receiver", "lol");
        final Message message = application.createMessage(sender, receiver.getName(), "Lol", new Date());
        final List<Message> messages = application.fetchMessages(receiver);
        assertEquals(Arrays.asList(message), messages);
    }

    @Test
    public void testFetchUser() throws UserAlreadyExistsException, UserNotFoundException {
        final User user = application.createUser("sender", "lol");
        final User user1 = application.fetchUser(user.getName());
        assertEquals(user, user1);
        try {
            application.fetchUser("unknown");
            fail();
        } catch (UserNotFoundException e) {
        }
    }
}

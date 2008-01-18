import com.nraynaud.sport.User;
import com.nraynaud.sport.UserAlreadyExistsException;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.hibernate.HibernateApplication;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Arrays;
import java.util.Date;


public class ApplicationTest {
    private HibernateApplication hibernateApplication;
    private EntityManager entityManager;

    @Before
    public void setUp() {
        final EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory("testPU");
        entityManager = managerFactory.createEntityManager();
        hibernateApplication = new HibernateApplication();
        hibernateApplication.setEntityManager(entityManager);
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
        final User user = hibernateApplication.createUser("lol", "pouet");
        final User user1 = hibernateApplication.fetchUser(user.getId());
        Assert.assertEquals(user, user1);
    }

    @Test
    public void testUserCreation() throws UserAlreadyExistsException {
        Assert.assertNull(hibernateApplication.authenticate("lolé", "pass+é"));
        hibernateApplication.createUser("lolé", "pass+é");
    }


    @Test
    public void testWorkoutCreation() throws UserAlreadyExistsException {
        Assert.assertNull(hibernateApplication.authenticate("lolé", "pass+é"));
        final User user = hibernateApplication.createUser("lolé", "pass+é");
        final Workout workout = hibernateApplication.createWorkout(new Date(), user, new Long(12), new Double(10),
                "lol");
        Assert.assertEquals(Arrays.asList(workout), hibernateApplication.fetchFrontPageData().getWorkouts());
    }

    @Test
    public void testUserAuth() throws UserAlreadyExistsException {
        Assert.assertNull(hibernateApplication.authenticate("lolé", "pass+é"));
        hibernateApplication.createUser("lolé", "pass+é");
        Assert.assertNotNull(hibernateApplication.authenticate("lolé", "pass+é"));
        Assert.assertNull(hibernateApplication.authenticate("lolé", "wrongpass"));
        Assert.assertNull(hibernateApplication.authenticate("jeanLouis", "wrongpass"));
    }

    @Test
    public void testUserSameName() throws UserAlreadyExistsException {
        hibernateApplication.createUser("lolé", "pass+é");
        try {
            hibernateApplication.createUser("lolé", "pass+é");
            Assert.fail("two users with same name where created");
        } catch (UserAlreadyExistsException e) {
            //great !
        }
    }

    @Test
    public void testWorkoutFetching() throws UserAlreadyExistsException {
        final User user = hibernateApplication.createUser("user", "lol");
        final Workout workout = hibernateApplication.createWorkout(new Date(), user, new Long(12), new Double(10),
                "lol");
        {
            final Workout workout1 = hibernateApplication.fetchWorkout(workout.getId(), user);
            Assert.assertEquals(workout, workout1);
        }
        {
            final Workout workout2 = hibernateApplication.fetchWorkout(Long.valueOf(1043345), user);
            Assert.assertNull(workout2);
        }
        {
            final User user1 = hibernateApplication.createUser("user1", "lol");
            final Workout workout3 = hibernateApplication.fetchWorkout(workout.getId(), user1);
            Assert.assertNull(workout3);
        }

    }
}

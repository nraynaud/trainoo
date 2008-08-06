import com.nraynaud.sport.*;
import com.nraynaud.sport.data.PaginatedCollection;
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
        final User user = application.createUser("lol", "pouet");
        final User user1 = application.fetchUser(user.getId());
        assertEquals(user, user1);
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
                application.fetchFrontPageData(0, 20, disciplines).statisticsData.workouts.iterator().next());
    }

    @Test
    public void testWorkoutGroupParticipation() throws NameClashException, AccessDeniedException {
        final User user = application.createUser("lolé", "pass+é");
        final User user2 = application.createUser("lolé2", "pass+é");
        final Workout workout = application.createWorkout(new Date(), user, new Long(12), new Double(10),
                null, "lol", null, null);
        final List<String> disciplines = Collections.emptyList();
        //application.setWorkoutParticipants(user, workout.getId(),
        //        new String[]{user.getName().nonEscaped(), user2.getName().nonEscaped()});
        final PaginatedCollection<Workout> workouts = application.fetchFrontPageData(0, 20, disciplines)
                .statisticsData.workouts;
        assertEquals(Arrays.asList(workout).iterator().next(), workouts.iterator().next());
        assertEquals(Arrays.asList(user, user2), workouts.iterator().next().getParticipants());
    }

    @Test
    public void testUserAuth() throws NameClashException {
        assertNull(application.authenticate("lolé", "pass+é", false));
        application.createUser("lolé", "pass+é");
        assertNotNull(application.authenticate("lolé", "pass+é", false));
        assertNull(application.authenticate("lolé", "wrongpass", false));
        assertNull(application.authenticate("jeanLouis", "wrongpass", false));
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
            final Workout workout1 = application.fetchWorkoutAndCheckUser(workout.getId(), user, true);
            assertEquals(workout, workout1);
        }
        {
            try {
                application.fetchWorkoutAndCheckUser(Long.valueOf(1043345), user, true);
                fail();
            } catch (WorkoutNotFoundException e) {
                //ok
            }
        }
        {
            final User user1 = application.createUser("user1", "lol");
            try {
                application.fetchWorkoutAndCheckUser(workout.getId(), user1, true);
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

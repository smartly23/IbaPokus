package cz.fi.muni.pa165.calorycounter.backend.dao;

import cz.fi.muni.pa165.calorycounter.backend.dao.impl.ActivityDaoImplJPA;
import cz.fi.muni.pa165.calorycounter.backend.model.Activity;
import javax.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import org.hamcrest.text.IsEqualIgnoringCase;
import org.junit.Test;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * Unit Tests on ActivityDaoImplJPA class using in-memory database and JPA
 * implementation to avoid mutual dependence among tested methods.
 *
 * @author Martin Pasko (smartly23)
 */
public class ActivityDaoTest {

    private static EntityManagerFactory emf;
    private EntityManager context;
    EntityManager another_context;
    private ActivityDao activityDao;
    final static Logger log = LoggerFactory.getLogger(ActivityDaoTest.class);

    @BeforeClass
    public static void setUpOnce() {
        emf = Persistence.createEntityManagerFactory("TestPU");
    }

    @AfterClass
    public static void tearDownOnce() {
        if (emf != null) {
            emf.close();
        }
    }

    @Before
    public void setUp() {   // doporucuje sa per-method inicializaciu robit v setUp radsej ako v Konstruktore
        context = emf.createEntityManager();
        activityDao = new ActivityDaoImplJPA(context);
    }

    @After
    public void tearDown() {
        context.close();
        if (another_context != null && another_context.isOpen()) {
            another_context.close();
        }
        activityDao = null;
    }

    @Test
    public void testCreate() {
        Activity activity = new Activity();
        activity.setName("Chopping wood sloww");
        activity.setDeleted(false);

        context.getTransaction().begin();
        Long activityId = activityDao.create(activity);
        context.getTransaction().commit();
        assertFalse("Activity was not created.", activityId == null);

        try {
            activityDao.create(null);
            fail("No exception thrown with null argument");
        } catch (IllegalArgumentException iae) {
        }

    }

    @Test
    public void testGet() {
        Activity activity = new Activity();
        activity.setName("Chopping wood slow");
        activity.setDeleted(false);

        Long activityId;
        try {
            context.getTransaction().begin();
            context.persist(activity);
            context.getTransaction().commit();
            activityId = activity.getId();
        } catch (Exception ex) {
            throw new RuntimeException("internal integrity error", ex);
        }
        assertNotNull("internal integrity error", activityId);

        another_context = emf.createEntityManager();              // kvoli cache lvl 1
        ActivityDao activityDao2 = new ActivityDaoImplJPA(another_context);
        Activity testActivity = activityDao2.get(activityId);
        assertEquals(activityId, testActivity.getId());

        try {
            activityDao2.get(activityId + 1);
            fail("Should throw exception when non-existent id is entered.");
        } catch (IllegalArgumentException iae) {
        }

    }

    @Test
    public void testUpdate() {
        Activity activity = new Activity();
        activity.setName("Chopping wood slowwww");
        activity.setDeleted(false);

        Long activityId;
        try {
            context.getTransaction().begin();
            context.persist(activity);
            context.getTransaction().commit();
            activityId = activity.getId();
        } catch (Exception ex) {
            throw new RuntimeException("internal integrity error", ex);
        }
        assertNotNull("internal integrity error", activityId);

        activity.setName("Chopping wood fast");
        context.getTransaction().begin();
        activityDao.update(activity);
        context.getTransaction().commit();

        another_context = emf.createEntityManager();              // to avoid returning result from cache lvl 1
        ActivityDao activityDao2 = new ActivityDaoImplJPA(another_context);
        Activity testActivity = another_context.find(Activity.class, activityId);
        assertThat(testActivity.getName(), IsEqualIgnoringCase.equalToIgnoringCase(activity.getName()));

        activity.setId(activityId + 1);
        try {
            activityDao.update(activity);
            fail("Should throw exception when non-existent id is entered.");
        } catch (IllegalArgumentException iae) {
            try {
                activityDao.update(null);
                fail("Should throw exception when null argument is entered.");
            } catch (IllegalArgumentException iaex) {
            }
        }

    }

    @Test
    public void testRemove() {
        Activity activity = new Activity();
        activity.setName("Chopping wood slowww");
        activity.setDeleted(false);

        context.getTransaction().begin();
        context.persist(activity);
        context.getTransaction().commit();

        another_context = emf.createEntityManager();              // to avoid returning result from cache lvl 1
        ActivityDao activityDao2 = new ActivityDaoImplJPA(another_context);
        Activity testActivity = another_context.find(Activity.class, activity.getId());
        // veryfying, that it is indeed in the database now:
        assertNotNull(testActivity);

        context.getTransaction().begin();
        activityDao.remove(activity.getId());
        context.getTransaction().commit();

        another_context.clear();                    // preco nefunguje aj em2.flush (obalene v transakcii)?
        assertFalse(!another_context.find(Activity.class, activity.getId()).isDeleted());
    }
}

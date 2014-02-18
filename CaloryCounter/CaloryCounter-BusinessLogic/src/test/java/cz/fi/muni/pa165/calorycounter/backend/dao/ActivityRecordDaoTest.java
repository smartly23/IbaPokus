package cz.fi.muni.pa165.calorycounter.backend.dao;

import cz.fi.muni.pa165.calorycounter.backend.dao.impl.ActivityRecordDaoImplJPA;
import cz.fi.muni.pa165.calorycounter.backend.model.Activity;
import cz.fi.muni.pa165.calorycounter.backend.model.ActivityRecord;
import cz.fi.muni.pa165.calorycounter.backend.model.AuthUser;
import cz.fi.muni.pa165.calorycounter.backend.model.Calories;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.sql.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Jan Kucera (Greld)
 */
public class ActivityRecordDaoTest {

    private static EntityManagerFactory emf;
    private static EntityManager context;
    private static ActivityRecordDao activityRecordDao;
    private static ActivityRecord activityRecord;
    private static AuthUser authUser;
    private static Calories calories;
    final static Logger log = LoggerFactory.getLogger(ActivityRecordDaoTest.class);

    @BeforeClass
    public static void before() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        context = emf.createEntityManager();
        activityRecordDao = new ActivityRecordDaoImplJPA(context);
        prepareTestEntities();
    }

    @AfterClass
    public static void tearDownClass() {
        if (emf != null) {
            emf.close();
        }
    }

    @Test
    public void testCreate() {
        setNewActivityRecord();
        context.getTransaction().begin();
        Long activityRecordId = activityRecordDao.create(activityRecord);
        context.getTransaction().commit();
        assertFalse("Activity record was not created.", activityRecordId == null);
        try {
            activityRecordDao.create(null);
            fail("No exception thrown with null argument");
        } catch (IllegalArgumentException iae) {
        }
    }

    @Test
    public void testGet() {
        setNewActivityRecord();
        Long activityRecordId;
        try {
            context.getTransaction().begin();
            context.persist(activityRecord);
            context.getTransaction().commit();
            activityRecordId = activityRecord.getId();
        } catch (Exception ex) {
            throw new RuntimeException("internal integrity error", ex);
        }
        assertNotNull("ID is null", activityRecordId);
        assertFalse("ID is 0", activityRecordId == 0);

        ActivityRecord activityRecordReturned = activityRecordDao.get(activityRecordId);
        assertNotNull("Activity record was not received by id", activityRecordReturned);
    }

    @Test
    public void testUpdate() {
        setNewActivityRecord();
        try {
            context.getTransaction().begin();
            context.persist(activityRecord);
            context.getTransaction().commit();
            assertNotNull("internal integrity error", activityRecord.getId());
        } catch (Exception ex) {
            throw new RuntimeException("internal integrity error", ex);
        }

        activityRecord.setActivityDate(Date.valueOf("1999-12-24"));
        activityRecord.setDuration(12);
        activityRecord.setCaloriesBurnt(112);

        context.getTransaction().begin();
        activityRecordDao.update(activityRecord);
        context.getTransaction().commit();

        context.clear();
        ActivityRecord activityRecord2 = context.find(ActivityRecord.class, activityRecord.getId());

        assertEquals(activityRecord2.getActivityDate(), activityRecord.getActivityDate());
        assertEquals(activityRecord2.getDuration(), activityRecord.getDuration());
        assertEquals(activityRecord2.getCaloriesBurnt(), activityRecord.getCaloriesBurnt());

        activityRecord.setId(activityRecord.getId() + 1);
        try {
            context.getTransaction().begin();
            activityRecordDao.update(activityRecord);
            context.getTransaction().commit();
            fail("Should throw exception when non-existent id is entered.");
        } catch (IllegalArgumentException iae) {
            try {
                activityRecordDao.update(null);
                fail("Should throw exception when null argument is entered.");
            } catch (IllegalArgumentException iaex) {
            }
        }

    }

    @Test
    public void testRemove() {
        setNewActivityRecord();
        context.getTransaction().begin();
        context.persist(activityRecord);
        context.getTransaction().commit();

        context.clear();

        ActivityRecord activityRecord2 = context.find(ActivityRecord.class, activityRecord.getId());
        // veryfying, that it is indeed in the database now:
        assertNotNull(activityRecord2);

        context.getTransaction().begin();
        activityRecordDao.remove(activityRecord2.getId());
        context.getTransaction().commit();

        context.clear();
        assertNull(context.find(ActivityRecord.class, activityRecord.getId()));
    }

    private static void prepareTestEntities() {
        authUser = new AuthUser();
        authUser.setUsername("Greld2486");
        authUser.setGender("Male");
        authUser.setName("Jan Novák");
        authUser.setWeightCat(WeightCategory._130_);

        context.getTransaction().begin();
        context.persist(authUser);
        context.getTransaction().commit();

        calories = new Calories();
        Activity activity = new Activity();
        activity.setName("Plavání");
        activity.setDeleted(false);
        context.getTransaction().begin();
        context.persist(activity);
        context.getTransaction().commit();

        calories.setActivity(activity);
        calories.setAmount(150);
        calories.setWeightCat(WeightCategory._130_);
        context.getTransaction().begin();
        context.persist(calories);
        context.getTransaction().commit();

    }

    private void setNewActivityRecord() {
        activityRecord = new ActivityRecord();
        activityRecord.setDuration(60 * 20);
        activityRecord.setCaloriesBurnt(500);
        activityRecord.setActivityDate(Date.valueOf("2010-10-20"));
        activityRecord.setCalories(calories);
        activityRecord.setCaloriesBurnt(calories.getAmount());
        activityRecord.setAuthUser(authUser);
    }
}

package cz.fi.muni.pa165.calorycounter.backend.dao;

import cz.fi.muni.pa165.calorycounter.backend.dao.impl.CaloriesDaoImplJPA;
import cz.fi.muni.pa165.calorycounter.backend.model.Activity;
import cz.fi.muni.pa165.calorycounter.backend.model.Calories;
import cz.fi.muni.pa165.calorycounter.backend.model.CaloriesPK;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit Tests on CaloriesDaoImplJPA class using in-memory database and JPA
 * implementation to avoid mutual dependence among tested methods.
 *
 * @author Zdenek Lastuvka
 */
public class CaloriesDaoTest {

    private static EntityManagerFactory emf;
    private EntityManager context;
    private CaloriesDao caloriesDao;
    Activity activity;
    final static Logger log = LoggerFactory.getLogger(ActivityDaoTest.class);

    public CaloriesDaoTest() {
        emf = Persistence.createEntityManagerFactory("TestPU");
        context = emf.createEntityManager();
        caloriesDao = new CaloriesDaoImplJPA(context);
        activity = new Activity();
        activity.setName("Swimming");
        activity.setDeleted(false);
        context.getTransaction().begin();
        context.persist(activity);
        context.getTransaction().commit();
    }

    @AfterClass
    public static void tearDownClass() {
        if (emf != null) {
            emf.close();
        }
    }

    @After
    public void tearDown() {
        context.close();
        caloriesDao = null;
    }

    @Test
    public void testCreate() {
        Calories calories = new Calories();
        calories.setWeightCat(WeightCategory._130_);
        calories.setAmount(150);
        calories.setActivity(activity);

        context.getTransaction().begin();
        CaloriesPK caloriesPk = caloriesDao.create(calories);
        context.getTransaction().commit();
        assertFalse("Calories was not created.", caloriesPk == null);

        try {
            caloriesDao.create(null);
            fail("No exception thrown with null argument");
        } catch (IllegalArgumentException iae) {
        }

    }

    @Test
    public void testGet() {

        Calories calories = new Calories();
        calories.setWeightCat(WeightCategory._130_);
        calories.setAmount(150);
        calories.setActivity(activity);

        CaloriesPK caloriesPk = new CaloriesPK();
        try {
            context.getTransaction().begin();
            context.persist(calories);
            context.getTransaction().commit();
            caloriesPk.setActivity(calories.getActivity());
            caloriesPk.setWeightCat(calories.getWeightCat());
        } catch (Exception ex) {
            throw new RuntimeException("internal integrity error", ex);
        }
        assertNotNull("activity is null", caloriesPk.getActivity());
        assertNotNull("weight category is null", caloriesPk.getWeightCat());

        Calories calories2 = caloriesDao.get(caloriesPk);
        assertNotNull("Calories was not received by pk", calories2);
    }

    @Test
    public void testUpdate() {
        Calories calories = new Calories();
        calories.setWeightCat(WeightCategory._130_);
        calories.setAmount(150);
        calories.setActivity(activity);

        CaloriesPK caloriesPk = new CaloriesPK();
        try {
            context.getTransaction().begin();
            context.persist(calories);
            context.getTransaction().commit();
            caloriesPk.setActivity(calories.getActivity());
            caloriesPk.setWeightCat(calories.getWeightCat());
            assertNotNull("internal integrity error", calories.getActivity());
        } catch (Exception ex) {
            throw new RuntimeException("internal integrity error", ex);
        }

        calories.setAmount(50);

        context.getTransaction().begin();
        caloriesDao.update(calories);
        context.getTransaction().commit();

        context.clear();
        Calories calories2 = context.find(Calories.class, caloriesPk);

        assertEquals(calories2.getAmount(), calories.getAmount());
    }

    @Test
    public void testRemove() {
        Calories calories = new Calories();
        calories.setWeightCat(WeightCategory._130_);
        calories.setAmount(150);
        calories.setActivity(activity);

        context.getTransaction().begin();
        context.persist(calories);
        context.getTransaction().commit();

        context.clear();

        CaloriesPK pk = new CaloriesPK();
        pk.setActivity(calories.getActivity());
        pk.setWeightCat(calories.getWeightCat());
        Calories calories2 = context.find(Calories.class, pk);
        // veryfying, that it is indeed in the database now:
        assertNotNull(calories2);

        context.getTransaction().begin();
        caloriesDao.remove(pk);
        context.getTransaction().commit();

        context.clear();
        assertNull(context.find(Calories.class, pk));
    }

    /**
     * Test of getByActivityWeightCat method, of class CaloriesDao.
     */
    @Test
    public void testGetByActivityWeightCat() {
        Calories calories = new Calories();
        calories.setWeightCat(WeightCategory._130_);
        calories.setAmount(150);
        calories.setActivity(activity);

        CaloriesPK caloriesPk = new CaloriesPK();
        try {
            context.getTransaction().begin();
            context.persist(calories);
            context.getTransaction().commit();
            caloriesPk.setActivity(calories.getActivity());
            caloriesPk.setWeightCat(calories.getWeightCat());
        } catch (Exception ex) {
            throw new RuntimeException("internal integrity error", ex);
        }
        assertNotNull("activity is null", caloriesPk.getActivity());
        assertNotNull("weight category is null", caloriesPk.getWeightCat());

        Calories calories2 = caloriesDao.getByActivityWeightCat(activity, WeightCategory._130_);
        assertNotNull("Calories was not received by act. and WeightCategory", calories2);

        assertEquals(calories, calories2);

        try {
            caloriesDao.getByActivityWeightCat(null, null);
            fail("Should throw exception when null argument is entered.");
        } catch (IllegalArgumentException iaex) {
        }

    }
}

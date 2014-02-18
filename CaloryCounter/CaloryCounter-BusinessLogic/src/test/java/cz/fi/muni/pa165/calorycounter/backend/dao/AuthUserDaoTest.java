/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.backend.dao;

import cz.fi.muni.pa165.calorycounter.backend.dao.impl.UserDaoImplJPA;
import cz.fi.muni.pa165.calorycounter.backend.model.AuthUser;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito.*;

/**
 *
 * @author Martin Bryndza (martin.bryndza)
 */
public class AuthUserDaoTest {

    private static UserDao authUserDao = null;
    private static AuthUser luke = null;
    private static AuthUser anakin = null;
    private static AuthUser obiWan = null;
    private static EntityManager context = null;

    @BeforeClass
    public static void before() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("TestPU");
        context = emf.createEntityManager();
        authUserDao = new UserDaoImplJPA(context);
        prepareTestEntities();
    }

    @AfterClass
    public static void after() {
        if (context != null) {
            context.close();
        }
    }

    @Test
    public void testCreate() {
        AuthUser c3po = new AuthUser();
        c3po.setName("C-3PO");
        c3po.setAge(3);
        c3po.setGender("ROBOT");
        c3po.setUsername("c3po");
        c3po.setWeightCat(WeightCategory._205_);

        context.getTransaction().begin();
        Long returnedId = authUserDao.create(c3po);
        context.getTransaction().commit();
        assertTrue("User was not created.", (returnedId != null && !returnedId.equals(new Long(0))));

        AuthUser createdUser = getUserById(returnedId);
        assertNotNull("User was not received by id", createdUser);

        try {
            authUserDao.create(null);
            fail("Should have thrown an exception on null argument.");
        } catch (IllegalArgumentException e) {
            //OK
        }
    }

    @Test
    public void testUpdate() {
        AuthUser user = new AuthUser();
        user.setId(anakin.getId());
        user.setName("Darth Vader");
        user.setAge(33);
        user.setWeightCat(WeightCategory._180_);
        user.setGender("MALE");
        user.setUsername("vady");

        context.getTransaction().begin();
        authUserDao.update(user);
        context.getTransaction().commit();

        AuthUser updatedUser = getUserById(user.getId());

        assertTrue("Name was not updated.", updatedUser.getName().equals(user.getName()));
        assertTrue("Gender was not updated.", updatedUser.getGender().equals(user.getGender()));
        assertTrue("Username was not updated.", updatedUser.getUsername().equals(user.getUsername()));
        assertTrue("Age was not updated.", updatedUser.getAge() == user.getAge());
        assertTrue("WeightCategory was not updated.", updatedUser.getWeightCat().equals(user.getWeightCat()));

        user.setId(user.getId() + 100);
        try {
            authUserDao.update(user);
            fail("Should have thrown an exception when user has invalid id.");
        } catch (IllegalArgumentException e) {
            //OK
        }
        try {
            authUserDao.update(null);
            fail("Should have throw an exception on null argument.");
        } catch (IllegalArgumentException e) {
            //OK
        }
    }

    @Test
    public void testGet() {
        AuthUser returnedUser = authUserDao.get(luke.getId());
        assertNotNull("User was not received by id", returnedUser);

        try {
            authUserDao.get(returnedUser.getId() + 100);
            fail("Should have thrown an exception when user has invalid id.");
        } catch (IllegalArgumentException e) {
            //OK
        }
        try {
            authUserDao.get(null);
            fail("Should have thrown an exception on null argument.");
        } catch (IllegalArgumentException e) {
            //OK
        }
    }

    @Test
    public void testRemove() {
        context.getTransaction().begin();
        authUserDao.remove(obiWan.getId());
        context.getTransaction().commit();
        AuthUser deletedUser = getUserById(obiWan.getId());
        assertNull("User was not deleted", deletedUser);

        AuthUser user = new AuthUser();
        user.setId(obiWan.getId() + 100);
        try {
            authUserDao.remove(user.getId());
            fail("Should have thrown an exception when user has invalid id.");
        } catch (IllegalArgumentException e) {
            //OK
        }
        try {
            authUserDao.remove(null);
            fail("Should have thrown an exception on null argument.");
        } catch (IllegalArgumentException e) {
            //OK
        }
    }

    @Test
    public void testGetByUsername() {
        System.out.println("" + luke.getUsername());
        AuthUser userByUsername = authUserDao.getByUsername(luke.getUsername());
        assertTrue("User was not found by username", userByUsername != null);
        assertTrue("Username of the user returned is not correct", userByUsername.getUsername().equals(luke.getUsername()));
        try {
            authUserDao.getByUsername("notExistingUsername");
            fail("Should have thrown an exception when username does not exist.");
        } catch (NoResultException ex) {
        }
        try {
            authUserDao.getByUsername(null);
            fail("Should have thrown an exception on null argument.");
        } catch (IllegalArgumentException e) {
            //OK
        }

    }

    private static void prepareTestEntities() {
        luke = new AuthUser();
        luke.setName("Luke Skywalker");
        luke.setAge(30);
        luke.setGender("MALE");
        luke.setUsername("luke");
        luke.setWeightCat(WeightCategory._155_);

        anakin = new AuthUser();
        anakin.setName("Anakin Skywalker");
        anakin.setAge(10);
        anakin.setGender("MALE");
        anakin.setUsername("anakin");
        anakin.setWeightCat(WeightCategory._130_);

        obiWan = new AuthUser();
        obiWan.setName("Obi-Wan Kenobi");
        obiWan.setAge(50);
        obiWan.setGender("MALE");
        obiWan.setUsername("keny");
        obiWan.setWeightCat(WeightCategory._180_);

        context.getTransaction().begin();
        context.persist(luke);
        context.persist(anakin);
        context.persist(obiWan);
        context.getTransaction().commit();

    }

    private AuthUser getUserById(long id) {
        Query q = context.createQuery("SELECT a FROM AuthUser a WHERE a.id=:id", AuthUser.class).setParameter("id", id);

        try {
            return (AuthUser) q.getSingleResult();
        } catch (NoResultException | NonUniqueResultException e) {
            return null;
        }
    }
}

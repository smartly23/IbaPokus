package cz.fi.muni.pa165.calorycounter.backend.dao.impl;

import cz.fi.muni.pa165.calorycounter.backend.dao.CaloriesDao;
import cz.fi.muni.pa165.calorycounter.backend.model.Calories;
import cz.fi.muni.pa165.calorycounter.backend.model.Activity;
import cz.fi.muni.pa165.calorycounter.backend.model.CaloriesPK;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.util.List;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * JPA/Hibernate DAO implementation - for operations on the persistence layer on
 * Calories entities.
 *
 * @author Jan Kucera (Greld)
 */
@Repository
public class CaloriesDaoImplJPA implements CaloriesDao {

    final static Logger log = LoggerFactory.getLogger(CaloriesDaoImplJPA.class);
    // injected from Spring
    @PersistenceContext
    private EntityManager em;

    public CaloriesDaoImplJPA() {
    }

    // this is only for legacy compatibility with some old tests
    public CaloriesDaoImplJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public Calories getByActivityWeightCat(Activity activity, WeightCategory weightCat) {
        if (activity == null || activity.getId() == null) {
            throw new IllegalArgumentException("Invalid activity: null or id is null");
        }
        if (weightCat == null) {
            throw new IllegalArgumentException("Invalid weightCat: null");
        }
        TypedQuery<Calories> query;
        try {
            query = em.createQuery("SELECT tbl FROM Calories tbl "
                    + "WHERE tbl.activity = :activity and tbl.weightCat = :weightCat", Calories.class);
            query.setParameter("activity", activity);
            query.setParameter("weightCat", weightCat);
        } catch (NoResultException nrex) {
            throw new IllegalArgumentException("Invalid activity or weightCat: Calories nonexistent", nrex);
        }
        return query.getSingleResult();
    }

    @Override
    public CaloriesPK create(Calories calories) {
        if (validate(calories)) {
            throw new IllegalArgumentException("Invalid user: null or null username of user");
        }
        System.out.println("Creating " + calories.toString());
        log.debug("Creating " + calories.toString());
        try {
            em.merge(calories);
        } catch (EntityExistsException e) {
            return null;
        }
        log.debug("Created " + calories.toString());
        CaloriesPK pk = new CaloriesPK();
        pk.setActivity(calories.getActivity());
        pk.setWeightCat(calories.getWeightCat());
        return pk;
    }

    @Override
    public Calories get(CaloriesPK pk) {
        if (validatePk(pk)) {
            throw new IllegalArgumentException("Invalid primary key: " + pk);
        } else if (em.createQuery("SELECT tbl.amount FROM Calories tbl WHERE tbl.activity = :activity "
                + "AND tbl.weightCat = :weightCat", Integer.class)
                .setParameter("activity", pk.getActivity())
                .setParameter("weightCat", pk.getWeightCat())
                .getResultList().size() < 1) {
            throw new IllegalArgumentException("Invalid composit id: nonexistent");
        }
        return em.createQuery("SELECT tbl FROM Calories tbl "
                + "WHERE tbl.activity = :activity AND tbl.weightCat = :weightCat", Calories.class)
                .setParameter("activity", pk.getActivity())
                .setParameter("weightCat", pk.getWeightCat())
                .getSingleResult();
    }

    @Override
    public void update(Calories calories) {
        if (validate(calories)) {
            throw new IllegalArgumentException("Invalid calories: null or part of id missing: " + calories);
        } else if (em.createQuery("SELECT tbl.amount FROM Calories tbl WHERE tbl.activity = :activity "
                + "AND tbl.weightCat = :weightCat", Integer.class)
                .setParameter("activity", calories.getActivity())
                .setParameter("weightCat", calories.getWeightCat())
                .getResultList().size() < 1) {
            throw new IllegalArgumentException("Invalid calories: nonexistent");
        }
        em.merge(calories);
    }

    @Override
    public void remove(CaloriesPK pk) {
        if (validatePk(pk)) {
            throw new IllegalArgumentException("Invalid calories primary key: " + pk);
        }
        Calories foundCalories = em.find(Calories.class, pk);
        if (foundCalories == null) {
            log.error("Calories is not in DB");
        }
        em.remove(foundCalories);
    }

    private boolean validate(Calories calories) {
        return (calories == null || calories.getWeightCat() == null || calories.getActivity() == null);
    }

    private boolean validatePk(CaloriesPK pk) {
        return (pk == null || pk.getWeightCat() == null || pk.getActivity() == null);
    }

    @Override
    public List<Calories> getAll() {
        return em.createQuery("SELECT tbl FROM Calories tbl ORDER BY tbl.activity.name, tbl.weightCat", Calories.class).getResultList();
    }

    @Override
    public List<Calories> getByActivity(Activity activity) {
        if (activity == null || activity.getId() == null) {
            throw new IllegalArgumentException("Invalid activity: null or id is null");
        }
        TypedQuery<Calories> query;
        try {
            query = em.createQuery("SELECT tbl FROM Calories tbl "
                    + "WHERE tbl.activity = :activity", Calories.class);
            query.setParameter("activity", activity);
        } catch (NoResultException nrex) {
            throw new IllegalArgumentException("Invalid activity: Calories nonexistent");
        }
        return query.getResultList();
    }

    @Override
    public List<Calories> getActiveByWeightCategory(WeightCategory weightCategory) {
        if (weightCategory == null) {
            throw new IllegalArgumentException("Invalid weight category: null");
        }
        TypedQuery<Calories> query;
        try {
            query = em.createQuery("SELECT tbl FROM Calories tbl "
                    + "WHERE tbl.weightCat = :weightCategory AND tbl.activity.deleted = :falseValue", Calories.class);
            query.setParameter("weightCategory", weightCategory);
            query.setParameter("falseValue", false);
        } catch (NoResultException nrex) {
            throw new IllegalArgumentException("Invalid activity: Calories nonexistent", nrex);
        }
        return query.getResultList();
    }

    @Override
    public List<Calories> getActive() {
        return em.createQuery("SELECT tbl FROM Calories tbl WHERE tbl.activity.deleted = :falseValue "
                + "ORDER BY tbl.activity.name, tbl.weightCat", Calories.class).setParameter("falseValue", false).getResultList();
    }

    @Override
    public List<Calories> getDeleted() {
        return em.createQuery("SELECT tbl FROM Calories tbl WHERE tbl.activity.deleted = :trueValue "
                + "ORDER BY tbl.activity.name, tbl.weightCat", Calories.class).setParameter("trueValue", true).getResultList();
    }
}

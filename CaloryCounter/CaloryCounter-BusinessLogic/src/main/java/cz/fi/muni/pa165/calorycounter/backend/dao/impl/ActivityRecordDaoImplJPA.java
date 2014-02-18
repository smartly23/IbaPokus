package cz.fi.muni.pa165.calorycounter.backend.dao.impl;

import cz.fi.muni.pa165.calorycounter.backend.dao.ActivityRecordDao;
import cz.fi.muni.pa165.calorycounter.backend.model.ActivityRecord;
import cz.fi.muni.pa165.calorycounter.backend.model.AuthUser;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * JPA/Hibernate DAO implementation - for operations on the persistence layer on
 * ActivityRecord entities.
 *
 * @author Zdenek Lastuvka
 */
@Repository
public class ActivityRecordDaoImplJPA implements ActivityRecordDao {

    final static Logger log = LoggerFactory.getLogger(CaloriesDaoImplJPA.class);
    // injected from Spring
    @PersistenceContext
    private EntityManager em;

    public ActivityRecordDaoImplJPA() {
    }

    // this is only for legacy compatibility with some old tests
    public ActivityRecordDaoImplJPA(EntityManager em) {
        this.em = em;
    }

    @Override
    public Long create(ActivityRecord record) {
        if (validate(record)) {
            throw new IllegalArgumentException("Invalid record: null");
        }
        ActivityRecord createdRecord = em.merge(record);
        return createdRecord.getId();
    }

    @Override
    public ActivityRecord get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid id: null");
        } else if (em.createQuery("SELECT tbl.id FROM ActivityRecord tbl WHERE tbl.id = "
                + ":givenId", Long.class).setParameter("givenId", id).getResultList().size() < 1) {
            throw new IllegalArgumentException("Invalid id: nonexistent");
        }
        return em.createQuery("SELECT tbl FROM ActivityRecord tbl "
                + "WHERE tbl.id = :givenId", ActivityRecord.class).setParameter("givenId", id).getSingleResult();
    }

    @Override
    public void update(ActivityRecord record) {
        if (validate(record) || record.getId() == null) {
            throw new IllegalArgumentException("Invalid record: null or with no id.");
        }
        if (em.createQuery("SELECT tbl.id FROM ActivityRecord tbl WHERE tbl.id = "
                + ":givenId", Long.class).setParameter("givenId", record.getId()).getResultList().size() < 1) {
            throw new IllegalArgumentException("Invalid user: nonexistent");
        }
        em.merge(record);
    }

    @Override
    public void remove(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Invalid record: null or with no id.");
        }
        ActivityRecord activityRecord = em.find(ActivityRecord.class, id);
        if (activityRecord == null) {
            log.error("ActivityRecord is not in DB");
        }

        em.remove(activityRecord);
    }

    private boolean validate(ActivityRecord record) {
        return (record == null || record.getAuthUser() == null || record.getCalories() == null);
    }

    @Override
    public List<ActivityRecord> getAllActivityRecordsByUser(AuthUser authUser) {
        if (authUser == null || authUser.getId() == null) {
            throw new IllegalArgumentException("Invalid authUser: null or id is null");
        }
        TypedQuery<ActivityRecord> query;
        List<ActivityRecord> returnedActivityRecords;
        try {
            query = em.createQuery("SELECT tbl"
                    + " FROM ActivityRecord tbl WHERE tbl.authUser = :authUser"
                    + " ", ActivityRecord.class);
            query.setParameter("authUser", authUser);
            returnedActivityRecords = query.getResultList();
        } catch (NoResultException nrex) {
            throw new IllegalArgumentException("Invalid authUser: nonexistent");
        }
        return returnedActivityRecords;
    }
}

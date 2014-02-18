/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.backend.dao.impl;

import cz.fi.muni.pa165.calorycounter.backend.dao.UserStatsDao;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * This class receives burntcalories for every user. It is a separate DAO for
 * optimalization purposes to avoid extracting whole database for single query.
 *
 * @author Jan Kucera (Greld)
 */
@Repository
public class UserStatsDaoImplJPA implements UserStatsDao {

    final static Logger log = LoggerFactory.getLogger(UserDaoImplJPA.class);
    // injected from Spring
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<UserStats> getUsersStats() {
        TypedQuery<UserStats> query;
        List<UserStats> returnedUsers;
        try {
            query = em.createQuery("SELECT new cz.fi.muni.pa165.calorycounter.backend.dao.impl.UserStatsDaoImplJPA$UserStats(u.id, u.name, SUM(r.caloriesBurnt), SUM(r.duration)) "
                    + " FROM AuthUser u LEFT JOIN u.records r WHERE u.id = r.authUser.id"
                    + " GROUP BY u.id, u.name ORDER BY SUM(r.caloriesBurnt) DESC", UserStats.class);
            returnedUsers = query.getResultList();
        } catch (NoResultException nrex) {
            throw new IllegalArgumentException("No users in DB");
        }
        return returnedUsers;
    }

    public static class UserStats {

        private final long userId;
        private final String userName;
        private final int sumBurntCalories;
        private final int sumDuration;

        public UserStats(long userId, String userName, long sumBurntCalories, long sumDuration) {
            this.userId = userId;
            this.userName = userName;
            this.sumBurntCalories = (int) sumBurntCalories;
            this.sumDuration = (int) sumDuration;
        }

        public long getUserId() {
            return userId;
        }

        public int getSumDuration() {
            return sumDuration;
        }

        public int getSumBurntCalories() {
            return sumBurntCalories;
        }

        public String getUserName() {
            return userName;
        }
    }
}

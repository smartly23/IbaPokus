/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.backend.dao;

import cz.fi.muni.pa165.calorycounter.backend.dao.impl.UserStatsDaoImplJPA;
import java.util.List;

/**
 * DAO interface - for operations on the persistence layer on User statistics.
 *
 * @author Jan Kucera (Greld)
 */
public interface UserStatsDao {

    /**
     * @return list of users with their stats (e.g. burnt calories, time spent
     * in activities)
     */
    public List<UserStatsDaoImplJPA.UserStats> getUsersStats();
}

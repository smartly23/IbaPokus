package cz.fi.muni.pa165.calorycounter.backend.dao;

import cz.fi.muni.pa165.calorycounter.backend.model.ActivityRecord;
import cz.fi.muni.pa165.calorycounter.backend.model.AuthUser;
import java.util.List;

/**
 * DAO interface - for operations on the persistence layer on ActivityRecord
 * entities.
 *
 * @author Zdenek Lastuvka
 */
public interface ActivityRecordDao extends Dao<ActivityRecord, Long> {

    /**
     * Find all user's records of activities
     *
     * @param authUser
     * @return list of ActivityRecord
     */
    List<ActivityRecord> getAllActivityRecordsByUser(AuthUser authUser);
}

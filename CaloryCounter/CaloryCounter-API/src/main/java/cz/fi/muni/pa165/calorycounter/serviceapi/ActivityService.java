/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.serviceapi;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.io.IOException;
import java.util.List;

/**
 * User service interface for operations on Activity DTO.
 *
 * @author Martin Bryndza (martin-bryndza)
 */
public interface ActivityService extends Service<ActivityDto> {

    /**
     * Find activity by id
     *
     * @param activityId
     * @return ActivityDto
     */
    ActivityDto get(Long activityId);

    /**
     * Find activity by name of activity
     *
     * @param activityName
     * @return ActivityDto
     */
    ActivityDto get(String activityName);

    /**
     * Find all activities with number of calories burnt per hour. The list will
     * not contain activities that are marked as deleted.
     *
     * @return list of ActivityDto
     */
    List<ActivityDto> getActive();

    /**
     * Find all activities. The list will not contain activities that are marked
     * as deleted.
     *
     * @param weightCategory with number of calories burnt per hour by users in
     * given weight category
     * @return list of ActivityDto
     */
    List<ActivityDto> getActive(WeightCategory weightCategory);

    /**
     * Find all activities that are marked as deleted.
     *
     * @return list of deleted activities
     */
    List<ActivityDto> getDeleted();

    /**
     * Find all activities that are active or deleted.
     *
     * @return list of active and deleted activities
     */
    List<ActivityDto> getAll();

    /**
     * Updates the list of activities based on the page
     * http://www.nutristrategy.com/activitylist.htm. An activity is considered
     * to be the same based on its name.
     *
     * @param removeDeprecated true to remove activities that no longer appear
     * on the page
     * @return List of the activities that were added or updated.
     * @throws java.io.IOException When the page is not accessible.
     */
    List<ActivityDto> updateFromPage(boolean removeDeprecated) throws IOException;

}

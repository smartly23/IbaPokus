package cz.fi.muni.pa165.calorycounter.backend.dto.convert;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.convert.Convert;
import cz.fi.muni.pa165.calorycounter.backend.dao.ActivityDao;
import cz.fi.muni.pa165.calorycounter.backend.dao.ActivityRecordDao;
import cz.fi.muni.pa165.calorycounter.backend.dao.CaloriesDao;
import cz.fi.muni.pa165.calorycounter.backend.dao.UserDao;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityRecordDto;
import cz.fi.muni.pa165.calorycounter.backend.model.Activity;
import cz.fi.muni.pa165.calorycounter.backend.model.ActivityRecord;
import cz.fi.muni.pa165.calorycounter.backend.model.AuthUser;
import cz.fi.muni.pa165.calorycounter.backend.model.Calories;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Conversion between ActivityRecord DTO and entity back and forth.
 *
 * @author Martin Pasko (smartly23)
 */
@Component
public class ActivityRecordConvert implements Convert<ActivityRecord, ActivityRecordDto> {

    final static Logger log = LoggerFactory.getLogger(ActivityRecordConvert.class);
    @Autowired
    private ActivityRecordDao activityRecordDao;
    @Autowired
    private ActivityDao activityDao;
    @Autowired
    private CaloriesDao caloriesDao;
    @Autowired
    private UserDao userDao;

    /*
     * Converts ActivityRecord DTO to appropriate entity.
     * @param dto DTO sent from service layer
     * @return ActivityRecord entity
     * @throws IllegalArgumentException if user id in parameter "dto" is null
     */
    @Override
    public ActivityRecord fromDtoToEntity(ActivityRecordDto dto) {
        ActivityRecord entity;

        if (dto.getActivityRecordId() != null) {
            entity = activityRecordDao.get(dto.getActivityRecordId());
            entity.getCalories().getActivity().setName(dto.getActivityName());
            entity.getCalories().setWeightCat(dto.getWeightCategory());
        } else {
            entity = new ActivityRecord();
            // create new Calories object and fill it into our entity; transaction management in the calling class
            Calories calories = new Calories();
            Activity activity;
            try {
                activity = activityDao.get(dto.getActivityName());
            } catch (NoResultException e) {
                log.warn("Activity with name " + dto.getActivityName() + " was not found in database. A new activity will be created.");
                activity = new Activity();
                activity.setName(dto.getActivityName());
                activityDao.create(activity);
            }
            try {
                calories = caloriesDao.getByActivityWeightCat(activity, dto.getWeightCategory());
            } catch (NoResultException e) {
                log.warn("Calories with activity " + activity.getName() + " and weight category " + dto.getWeightCategory() + " was not found in database. A new calories will be created.");
                calories.setActivity(activity);
                calories.setWeightCat(dto.getWeightCategory());
                caloriesDao.create(calories);
            }
            entity.setCalories(calories);
        }

        if (dto.getUserId() != null) {
            AuthUser user = userDao.get(dto.getUserId());
            entity.setAuthUser(user);
            // conversion method is not responsible for checking any data consistence
        } else {
            log.error("ActivityRecord DTO-to-DAO conversion: converting to ActivityRecord entity "
                    + "but no user id given in DTO. User id is mandatory. Exception thrown.");
            throw new IllegalArgumentException("No userId in DTO: " + dto + "userId is mandatory.");
        }
        entity.setActivityDate(new java.sql.Date(dto.getActivityDate().getTime()));     // util.Date to sql.Date
        entity.setCaloriesBurnt(dto.getCaloriesBurnt());
        entity.setDuration(dto.getDuration());

        return entity;
    }

    /*
     * Converts ActivityRecord entity to appropriate DTO.
     * @param entity ActivityRecord entity to be converted to DTO
     * @return ActivityRecord DTO
     * @throws IllegalArgumentException if entity id in parameter "entity" is null
     */
    @Override
    public ActivityRecordDto fromEntityToDto(ActivityRecord entity) {
        ActivityRecordDto dto = new ActivityRecordDto();
        if (entity.getActivityDate() != null) {
            dto.setActivityDate(new java.util.Date(entity.getActivityDate().getTime()));    // sql.Date to util.Date
        }
        dto.setCaloriesBurnt(entity.getCaloriesBurnt());
        dto.setDuration(entity.getDuration());
        dto.setWeightCategory(entity.getCalories().getWeightCat());
        dto.setActivityName(entity.getCalories().getActivity().getName());
        dto.setUserId(entity.getAuthUser().getId());
        if (entity.getId() == null) {
            log.error("ActivityRecord DAO-to-DTO: Entity " + entity + "to be converted to DTO has no id!");
            throw new IllegalArgumentException("Entity " + entity + "to-be-converted to DTO has no id!");

        } else {
            dto.setActivityRecordId(entity.getId());
        }
        return dto;
    }

    public List<ActivityRecordDto> fromEntityToDto(List<ActivityRecord> activityRecords) {
        if (activityRecords == null) {
            return null;
        }
        List<ActivityRecordDto> activityRecordDtos = new ArrayList<>();
        for (ActivityRecord activityRecord : activityRecords) {
            activityRecordDtos.add(fromEntityToDto(activityRecord));
        }
        return activityRecordDtos;
    }

    public void setActivityRecordDao(ActivityRecordDao activityRecordDao) {
        this.activityRecordDao = activityRecordDao;
    }

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void setCaloriesDao(CaloriesDao caloriesDao) {
        this.caloriesDao = caloriesDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
}

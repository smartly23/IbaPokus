package cz.fi.muni.pa165.calorycounter.backend.service.impl;

import cz.fi.muni.pa165.calorycounter.backend.dao.ActivityRecordDao;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityRecordDto;
import cz.fi.muni.pa165.calorycounter.backend.dto.convert.ActivityRecordConvert;
import cz.fi.muni.pa165.calorycounter.backend.model.ActivityRecord;
import cz.fi.muni.pa165.calorycounter.serviceapi.ActivityRecordService;
import cz.fi.muni.pa165.calorycounter.backend.service.common.DataAccessExceptionNonVoidTemplate;
import cz.fi.muni.pa165.calorycounter.backend.service.common.DataAccessExceptionVoidTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

/**
 * User service for all operations on ActivityRecord DTO.
 *
 * @author Martin Pasko (smartly23)
 */
@Service
@Transactional
public class ActivityRecordServiceImpl implements ActivityRecordService {

    final static Logger log = LoggerFactory.getLogger(ActivityRecordConvert.class);
    @Autowired
    private ActivityRecordConvert convert;
    @Autowired
    private ActivityRecordDao activityRecordDao;

    /*
     * @throws IllegalArgumentException if dto with existing ActivityRecord (activityRecordId) is given.
     * @throws DataAccessException if operation failed on persistence layer. No transaction done.
     */
    @Override
    @Transactional(readOnly = false)
    public Long create(ActivityRecordDto dto) {
        if (dto.getActivityRecordId() != null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Cannot create activity record that"
                    + " already exists. Use update instead.");
            log.error("ActivityRecordServiceImpl.create() called on existing entity", iaex);
            throw iaex;
        }
        return (Long) new DataAccessExceptionNonVoidTemplate(dto) {
            @Override
            public Long doMethod() {
                ActivityRecord entity = convert.fromDtoToEntity((ActivityRecordDto) getU());
                Long entityId = activityRecordDao.create(entity);
                return entityId;
            }
        }.tryMethod();
    }

    /*
     * @throws DataAccessException if operation failed on persistence layer. No transaction done.
     */
    @Override
    public ActivityRecordDto get(Long id) {
        if (id == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Invalid id in parameter: null");
            log.error("ActivityRecordServiceImpl.get() called on null parameter: Long id", iaex);
            throw iaex;
        }
        return (ActivityRecordDto) new DataAccessExceptionNonVoidTemplate(id) {
            @Override
            public ActivityRecordDto doMethod() {
                ActivityRecord entity = activityRecordDao.get((Long) getU());
                ActivityRecordDto dto = convert.fromEntityToDto(entity);
                return dto;
            }
        }.tryMethod();
    }

    /*
     * @throws IllegalArgumentException if dto with non-existent ActivityRecord (activityRecordId) is given.
     * @throws DataAccessException if operation failed on persistence layer. No transaction done.
     */
    @Override
    @Transactional(readOnly = false)
    public void update(ActivityRecordDto dto) {
        if (dto.getActivityRecordId() == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Cannot update activity record that"
                    + " doesn't exist. Use create instead.");
            log.error("ActivityRecordServiceImpl.update() called on non-existent entity", iaex);
            throw iaex;
        } else {
            new DataAccessExceptionVoidTemplate(dto) {
                @Override
                public void doMethod() {
                    ActivityRecord entity = convert.fromDtoToEntity((ActivityRecordDto) getU());
                    activityRecordDao.update(entity);
                }
            }.tryMethod();
        }
    }

    /*
     * @throws IllegalArgumentException if dto with non-existent ActivityRecord (activityRecordId) is given.
     * @throws DataAccessException if operation failed on persistence layer. No transaction done.
     */
    @Override
    @Transactional(readOnly = false)
    public void remove(Long activityRecordId) {
        if (activityRecordId == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Cannot remove activity record that"
                    + " doesn't exist.");
            log.error("ActivityRecordServiceImpl.remove() called on non-existent entity", iaex);
            throw iaex;
        } else {
            new DataAccessExceptionVoidTemplate(activityRecordId) {
                @Override
                public void doMethod() {
                    activityRecordDao.remove((Long) getU());
                }
            }.tryMethod();
        }
    }

    public void setActivityRecordDao(ActivityRecordDao activityRecordDao) {
        this.activityRecordDao = activityRecordDao;
    }

    public void setConvert(ActivityRecordConvert convert) {
        this.convert = convert;
    }
}

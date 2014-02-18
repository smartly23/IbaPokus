/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.backend.service.impl;

import cz.fi.muni.pa165.calorycounter.serviceapi.ActivityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityDto;
import cz.fi.muni.pa165.calorycounter.backend.model.Activity;
import cz.fi.muni.pa165.calorycounter.backend.dao.ActivityDao;
import cz.fi.muni.pa165.calorycounter.backend.dao.CaloriesDao;
import cz.fi.muni.pa165.calorycounter.backend.dto.convert.ActivityConvert;
import cz.fi.muni.pa165.calorycounter.backend.model.Calories;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import cz.fi.muni.pa165.calorycounter.backend.service.common.DataAccessExceptionNonVoidTemplate;
import cz.fi.muni.pa165.calorycounter.backend.service.common.DataAccessExceptionVoidTemplate;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.NoResultException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author Martin Bryndza (martin-bryndza)
 */
@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    final static Logger log = LoggerFactory.getLogger(ActivityService.class);
    @Autowired
    private ActivityConvert convert;
    @Autowired
    private ActivityDao activityDao; //injected
    @Autowired
    private CaloriesDao caloriesDao; //injected

    public void setActivityDao(ActivityDao activityDao) {
        this.activityDao = activityDao;
    }

    public void setCaloriesDao(CaloriesDao caloriesDao) {
        this.caloriesDao = caloriesDao;
    }

    @Override
    public ActivityDto get(Long activityId) {
        if (activityId == null) {
            String msg = "ActivityId is null";
            IllegalArgumentException ex = new IllegalArgumentException(msg);
            log.error(msg, ex);
            throw ex;
        }
        return (ActivityDto) new DataAccessExceptionNonVoidTemplate(activityId) {
            @Override
            public ActivityDto doMethod() {
                Activity activity = activityDao.get((Long) getU());
                List<Calories> cals = new LinkedList<>();
                for (WeightCategory wc : WeightCategory.values()) {
                    Calories cal = caloriesDao.getByActivityWeightCat(activity, wc);
                    cals.add(cal);
                }
                ActivityDto dto = convert.fromEntitiesListToDto(cals);
                return dto;
            }
        }.tryMethod();
    }

    @Override
    public ActivityDto get(String activityName) {
        if (activityName == null || activityName.isEmpty()) {
            String msg = "ActivityName is null or empty: \"" + activityName + "\"";
            IllegalArgumentException ex = new IllegalArgumentException(msg);
            log.error(msg, ex);
            throw ex;
        }
        return (ActivityDto) new DataAccessExceptionNonVoidTemplate(activityName) {
            @Override
            public ActivityDto doMethod() {
                Activity activity = activityDao.get((String) getU());
                List<Calories> cals = new LinkedList<>();
                for (WeightCategory wc : WeightCategory.values()) {
                    Calories cal = caloriesDao.getByActivityWeightCat(activity, wc);
                    cals.add(cal);
                }
                ActivityDto dto = convert.fromEntitiesListToDto(cals);
                return dto;
            }
        }.tryMethod();
    }

    @Override
    public List<ActivityDto> getActive() {
        List<Calories> cals = caloriesDao.getActive();
        return getDtosFromCalories(cals);
    }

    @Override
    public List<ActivityDto> getActive(WeightCategory weightCategory) {
        List<Calories> cals = caloriesDao.getActiveByWeightCategory(weightCategory);
        return getDtosFromCalories(cals);
    }

    private List<ActivityDto> getDtosFromCalories(List<Calories> cals) {
        List<ActivityDto> dtos = new LinkedList<>();
        Map<Activity, List<Calories>> calsByActivity = new LinkedHashMap<>();
        for (Calories cal : cals) {
            if (!calsByActivity.containsKey(cal.getActivity())) {
                calsByActivity.put(cal.getActivity(), new LinkedList<Calories>());
            }
            calsByActivity.get(cal.getActivity()).add(cal);
        }
        for (Activity act : calsByActivity.keySet()) {
            dtos.add(convert.fromEntitiesListToDto(calsByActivity.get(act)));
        }
        return dtos;
    }

    @Override
    public List<ActivityDto> updateFromPage(boolean removeDeprecated) throws IOException {
        Document doc = Jsoup.connect("http://www.nutristrategy.com/activitylist.htm").get();
        Elements activityElements = doc.select("blockquote div div table tbody tr");
        activityElements.set(0, null); // the first row is head of table
        List<ActivityDto> activities = new LinkedList<>(); // list to be returned
        List<Activity> originalActivities = activityDao.getActive(); //at the end this list will contain activities that were not found on the page
        for (Element activityElement : activityElements) {
            if (activityElement == null) {
                continue;
            }

            Elements dataElements = activityElement.getElementsByTag("td");
            List<String> data = new LinkedList<>(); // contains name of activity and 130lbs, 155lbs, 180lbs, 205lbs weight categories in this order
            for (Element dataElement : dataElements) {
                data.add(dataElement.text());
            }

            List<Calories> calories = new LinkedList<>(); // list of calories to be converted to DTO for return
            Activity activity;
            // try to get recent activities from DB
            try {
                activity = activityDao.get(data.get(0));
                if (activity.isDeleted()) {
                    activity.setDeleted(false);
                    activityDao.update(activity);
                }
            } catch (IllegalArgumentException e) {
                activity = new Activity();
                activity.setName(data.get(0));
                activity.setDeleted(false);
                activity.setId(activityDao.create(activity));
            }
            originalActivities.remove(activity);

            for (int i = 1; i < data.size(); i++) {
                WeightCategory weight = WeightCategory.getCategory(i - 1);
                int amount = Integer.parseInt(data.get(i));
                Calories calory;
                try {
                    calory = caloriesDao.getByActivityWeightCat(activity, weight);
                } catch (NoResultException e) {
                    calory = new Calories();
                    calory.setActivity(activity);
                    calory.setWeightCat(weight);
                    calory.setAmount(amount);
                    calories.add(calory);
                    log.debug("Creating calories " + calory.toString());
                    caloriesDao.create(calory);
                    log.debug("Calories created.");
                    continue;
                }
                if (calory.getAmount() != amount || activity.isDeleted()) { // does the calory need to be updated?
                    calory.setAmount(amount);
                    calories.add(calory);
                    log.debug("Updating calories " + calory.toString());
                    caloriesDao.update(calory);
                    log.debug("Calories updated.");
                } else {
                    log.debug("Calories are up to date: " + calory);
                }
            }
            activities.add(convert.fromEntitiesListToDto(calories));
        }

        if (removeDeprecated) {
            // set all remaining original activities that were not found on the page as deleted
            for (Activity activity : originalActivities) {
                activity.setDeleted(true);
                activityDao.update(activity);
            }
        }
        return activities;
    }

    @Override
    public List<ActivityDto> getDeleted() {
        List<Calories> cals = caloriesDao.getDeleted();
        return getDtosFromCalories(cals);
    }

    @Override
    public List<ActivityDto> getAll() {
        List<Calories> cals = caloriesDao.getAll();
        return getDtosFromCalories(cals);
    }

    @Override
    public Long create(ActivityDto dto) {
        if (dto.getActivityId() != null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Cannot create activity that"
                    + " already exists. Use update instead.");
            log.error("create() called on existing entity", iaex);
            throw iaex;
        }
        return (Long) new DataAccessExceptionNonVoidTemplate(dto) {
            @Override
            public Long doMethod() {
                List<Calories> entities = convert.fromDtoToEntitiesList((ActivityDto) getU());
                Activity activity = entities.get(0).getActivity();
                Long entityId = activityDao.create(activity);
                for (Calories calory : entities) {
                    calory.getActivity().setId(entityId);
                    caloriesDao.create(calory);
                }
                return entityId;
            }
        }.tryMethod();
    }

    @Override
    public void update(ActivityDto dto) {
        if (dto.getActivityId() == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Cannot update activity that"
                    + " doesn't exist. Use create instead.");
            log.error("update() called on non-existent entity", iaex);
            throw iaex;
        } else {
            new DataAccessExceptionVoidTemplate(dto) {
                @Override
                public void doMethod() {
                    List<Calories> entities = convert.fromDtoToEntitiesList((ActivityDto) getU());
                    Activity activity = entities.get(0).getActivity();
                    activityDao.update(activity);
                    for (Calories calory : entities) {
                        caloriesDao.update(calory);
                    }
                }
            }.tryMethod();
        }
    }

    @Override
    public void remove(Long id) {
        if (id == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Cannot remove activity that"
                    + " doesn't exist.");
            log.error("remove() called on non-existent entity", iaex);
            throw iaex;
        } else {
            new DataAccessExceptionVoidTemplate(id) {
                @Override
                public void doMethod() {
                    activityDao.remove((Long) getU());
                }
            }.tryMethod();
        }
    }
}

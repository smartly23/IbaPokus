/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.backend.service;

import cz.fi.muni.pa165.calorycounter.serviceapi.ActivityService;
import cz.fi.muni.pa165.calorycounter.backend.dao.ActivityDao;
import cz.fi.muni.pa165.calorycounter.backend.dao.CaloriesDao;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityDto;
import cz.fi.muni.pa165.calorycounter.backend.dto.convert.ActivityConvert;
import cz.fi.muni.pa165.calorycounter.backend.model.Activity;
import cz.fi.muni.pa165.calorycounter.backend.model.Calories;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import cz.fi.muni.pa165.calorycounter.backend.service.impl.ActivityServiceImpl;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;
import static org.junit.Assert.*;

/**
 *
 * @author Martin Bryndza (martin-bryndza)
 */
@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceTest {

    @InjectMocks
    ActivityService activityService = new ActivityServiceImpl();
    @Mock
    private CaloriesDao caloriesDaoMock;
    @Mock
    private ActivityDao activityDaoMock;
    @Mock
    private ActivityConvert activityConvertMock;
    private Activity activity;
    private ActivityDto activityDto;
    private Map<WeightCategory, Calories> caloriesOfActivity;

    @Before
    public void setUp() {
        activity = new Activity();
        activity.setId(54L);
        activity.setName("Cutting wood");
        activity.setDeleted(false);

        caloriesOfActivity = new HashMap<>();
        for (WeightCategory wc : WeightCategory.values()) {
            Calories cal = new Calories();
            cal.setActivity(activity);
            cal.setWeightCat(wc);
            cal.setAmount((wc.ordinal() + 1) * 10);
            caloriesOfActivity.put(wc, cal);
        }

        activityDto = new ActivityDto();
        activityDto.setActivityName(activity.getName());
        for (Calories cal : caloriesOfActivity.values()) {
            activityDto.setCaloriesAmount(cal.getWeightCat(), cal.getAmount());
        }

    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetById() {
        when(activityDaoMock.get(activity.getId())).thenReturn(activity);
        for (WeightCategory wc : WeightCategory.values()) {
            when(caloriesDaoMock.getByActivityWeightCat(activity, wc)).thenReturn(caloriesOfActivity.get(wc));
        }
        when(activityConvertMock.fromEntitiesListToDto(anyList())).thenReturn(activityDto);

        ActivityDto comparedDto = activityService.get(activity.getId());

        assertEquals("Get method does not return expected DTO", activityDto, comparedDto);
    }

    @Test
    public void testGetByName() {
        when(activityDaoMock.get(activity.getName())).thenReturn(activity);
        for (WeightCategory wc : WeightCategory.values()) {
            when(caloriesDaoMock.getByActivityWeightCat(activity, wc)).thenReturn(caloriesOfActivity.get(wc));
        }
        when(activityConvertMock.fromEntitiesListToDto(anyList())).thenReturn(activityDto);

        ActivityDto comparedDto = activityService.get(activity.getName());

        assertEquals("Get method does not return expected DTO", activityDto, comparedDto);
    }

    @Test
    public void testGetAll() {
        when(caloriesDaoMock.getAll()).thenReturn(new LinkedList<>(caloriesOfActivity.values()));
        when(activityConvertMock.fromEntitiesListToDto(new LinkedList<>(caloriesOfActivity.values()))).thenReturn(activityDto);

        List<ActivityDto> comparedDtos = activityService.getAll();
        List<ActivityDto> expectededDtos = new LinkedList<>();
        expectededDtos.add(activityDto);

        assertEquals("Get method does not return expected DTO", expectededDtos, comparedDtos);
    }
}

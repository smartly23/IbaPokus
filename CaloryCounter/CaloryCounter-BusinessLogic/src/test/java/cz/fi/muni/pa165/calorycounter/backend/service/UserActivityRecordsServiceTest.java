package cz.fi.muni.pa165.calorycounter.backend.service;

import cz.fi.muni.pa165.calorycounter.serviceapi.UserActivityRecordsService;
import cz.fi.muni.pa165.calorycounter.backend.dao.impl.ActivityRecordDaoImplJPA;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityRecordDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserActivityRecordsDto;
import cz.fi.muni.pa165.calorycounter.backend.dto.convert.ActivityRecordConvert;
import cz.fi.muni.pa165.calorycounter.backend.model.ActivityRecord;
import cz.fi.muni.pa165.calorycounter.backend.model.AuthUser;
import cz.fi.muni.pa165.calorycounter.backend.model.Calories;
import cz.fi.muni.pa165.calorycounter.backend.service.impl.UserActivityRecordsServiceImpl;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit Tests using Mockito to mock DAO layer and thus avoid real DB operations.
 *
 * @author lastuvka
 */
@RunWith(MockitoJUnitRunner.class)
public class UserActivityRecordsServiceTest {

    @InjectMocks
    private UserActivityRecordsService uars = new UserActivityRecordsServiceImpl();
    @Mock
    private ActivityRecordDaoImplJPA activityRecordDaoImplJPA;
    @Mock
    private ActivityRecordConvert activityRecordConvertMock;
    @Mock
    private Calories caloriesMock;
    private AuthUser user;
    private AuthUserDto userDto;
    private ActivityRecord activityRecord;
    private ActivityRecordDto activityRecordDto;
    private UserActivityRecordsDto uard;
    private final Long ACTIVITY_RECORD_ID = 42L;
    private final Long USER_ID = 237L;
    ArrayList<ActivityRecord> al;
    ArrayList<ActivityRecordDto> alDto;

    public UserActivityRecordsServiceTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

        Long time = new java.util.Date().getTime();

        user = new AuthUser();
        user.setId(USER_ID);
        user.setName("Edita Papeky");

        userDto = new AuthUserDto();
        userDto.setUserId(USER_ID);
        userDto.setName("Edita Papeky");

        activityRecord = new ActivityRecord();
        activityRecord.setId(ACTIVITY_RECORD_ID);
        activityRecord.setActivityDate(new java.sql.Date(time));
        activityRecord.setDuration(40);
        activityRecord.setCaloriesBurnt(4000);
        activityRecord.setCalories(caloriesMock);
        activityRecord.setAuthUser(user);

        activityRecordDto = new ActivityRecordDto();
        activityRecordDto.setActivityName("Strielanie zajacov.");
        activityRecordDto.setActivityDate(new java.util.Date(time));
        activityRecordDto.setCaloriesBurnt(4000);
        activityRecordDto.setDuration(40);
        activityRecordDto.setWeightCategory(WeightCategory._180_);
        activityRecordDto.setUserId(USER_ID);
        activityRecordDto.setActivityRecordId(ACTIVITY_RECORD_ID);

        al = new ArrayList<ActivityRecord>();
        al.add(activityRecord);

        alDto = new ArrayList<ActivityRecordDto>();
        alDto.add(activityRecordDto);

        uard = new UserActivityRecordsDto();
        uard.setActivityRecords(alDto);
        uard.setNameOfUser(user.getName());

    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getAllActivityRecords method, of class
     * UserActivityRecordsService.
     */
    @Test
    public void testGetAllActivityRecords() {
        when(activityRecordConvertMock.fromEntityToDto(al)).thenReturn(alDto);

        Mockito.stub(activityRecordDaoImplJPA.getAllActivityRecordsByUser(user)).toReturn(al);
        UserActivityRecordsDto uard2 = uars.getAllActivityRecords(userDto);
        assertEquals("Name is not equals", uard.getNameOfUser(), uard2.getNameOfUser());
        assertEquals("diffrent list of activities", uard.getActivityRecords(), uard2.getActivityRecords());
        try {
            uars.getAllActivityRecords(null);
            fail("uars.getAllActivityRecords with null dont throw exception");
        } catch (IllegalArgumentException e) {
        }
        try {
            userDto.setName(null);
            uars.getAllActivityRecords(userDto);
            fail("uars.getAllActivityRecords with null name dont throw exception");
        } catch (IllegalArgumentException e) {
        }
    }
}

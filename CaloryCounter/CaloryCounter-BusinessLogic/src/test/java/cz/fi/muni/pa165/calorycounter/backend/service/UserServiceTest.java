/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.backend.service;

import cz.fi.muni.pa165.calorycounter.serviceapi.UserService;
import cz.fi.muni.pa165.calorycounter.backend.dao.impl.UserDaoImplJPA;
import cz.fi.muni.pa165.calorycounter.backend.dao.impl.UserStatsDaoImplJPA;
import cz.fi.muni.pa165.calorycounter.backend.dao.impl.UserStatsDaoImplJPA.UserStats;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserStatsDto;
import cz.fi.muni.pa165.calorycounter.backend.model.AuthUser;
import cz.fi.muni.pa165.calorycounter.backend.service.impl.UserServiceImpl;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.NoResultException;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.RecoverableDataAccessException;
import sun.java2d.loops.CompositeType;

/**
 * Unit Tests using Mockito to mock DAO layer and thus avoid real DB operations.
 *
 * @author Jan Kucera (Greld)
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private final UserService userService = new UserServiceImpl();
    @Mock
    private UserDaoImplJPA userDaoImplJPA;
    @Mock
    private UserStatsDaoImplJPA userStatsDaoImplJPA;
    private AuthUser user;
    private AuthUserDto userDto;
    private final Long USER_ID = 237L;
    private final String USERNAME = "edita";
    private final String PASSWORD = "heslo";
    private final List<UserStats> listStats = new ArrayList<>();

    public UserServiceTest() {

    }

    @Before
    public void setUp() {
        user = new AuthUser();
        user.setId(USER_ID);
        user.setName("Edita Papeky");
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        userDto = new AuthUserDto();
        userDto.setUserId(USER_ID);
        userDto.setName("Edita Papeky");
        userDto.setUsername(USERNAME);
        userDto.setAge(1);
        userDto.setSex("Male");
        userDto.setWeightCategory(WeightCategory._130_);
        userService.register(userDto, PASSWORD);

        UserStatsDto userStatsDto = new UserStatsDto();
        userStatsDto.setNameOfUser(USERNAME);

        UserStats userStats = new UserStats(USER_ID, USERNAME, 150, 10);

        listStats.add(userStats);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetByUsername() {
        Mockito.stub(userDaoImplJPA.getByUsername(USERNAME)).toReturn(user);
        AuthUserDto uDto = userService.getByUsername(USERNAME);
        assertNotNull("User was not found by username.", uDto);
        assertEquals("Wrong user was found by username.", userDto, uDto);
    }

    @Test
    public void testLogin() {
        Mockito.stub(userDaoImplJPA.login(eq(USERNAME), any(String.class))).toReturn(user);
        AuthUserDto uDto = userService.login(USERNAME, PASSWORD);
        assertNotNull("User was not login.", uDto);
        assertEquals("Wrong user was login.", userDto, uDto);
    }

    @Test
    public void testRegister() {
        Long nextId = new Long(USER_ID + 1);
        Mockito.stub(userDaoImplJPA.create(user)).toReturn(nextId);
        Mockito.stub(userDaoImplJPA.getByUsername(USERNAME)).toReturn(user);
        Long id = userService.register(userDto, PASSWORD);
        assertNotNull("User was not registered.", id);
        assertEquals("Wrong id was returned.", nextId, id);
    }

    @Test
    public void testChangePassword() {
        Mockito.stub(userDaoImplJPA.getByUsername(eq(USERNAME))).toReturn(user);
        userService.setPassword(USERNAME, "new" + PASSWORD);
    }

    @Test
    public void testChangeEmptyPassword() {
        Mockito.stub(userDaoImplJPA.getByUsername(eq(USERNAME))).toReturn(user);
        userService.setPassword(USERNAME, "");
    }

    @Test
    public void testChangeNullPassword() {
        Mockito.stub(userDaoImplJPA.getByUsername(eq(USERNAME))).toReturn(user);
        userService.setPassword(USERNAME, null);
    }

    @Test
    public void testChangeWrongUserPassword() {
        Mockito.stub(userDaoImplJPA.getByUsername(eq(USERNAME + "wrong"))).toThrow(new RecoverableDataAccessException("Stubbed operation login failed"));
        try {
            userService.setPassword(USERNAME + "wrong", "anything");
        } catch (RecoverableDataAccessException e) {
            return;
        }
        fail("Should have thrown RecoverableDataAccessException.");
    }

    @Test
    public void testGetAllUserStats() {
        Mockito.stub(userStatsDaoImplJPA.getUsersStats()).toReturn(listStats);

        List<UserStatsDto> listDto = userService.getAllUserStats();
        assertNotNull("List is null.", listDto);
        assertFalse("List is empty", listDto.isEmpty());
        assertTrue("List's size should be 1", listDto.size() == 1);

        assertEquals("User is not in the list", USERNAME, listDto.iterator().next().getNameOfUser());
    }

}

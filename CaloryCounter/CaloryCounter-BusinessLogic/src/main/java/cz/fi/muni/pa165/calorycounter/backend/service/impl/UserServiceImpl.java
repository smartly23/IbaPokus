package cz.fi.muni.pa165.calorycounter.backend.service.impl;

import cz.fi.muni.pa165.calorycounter.backend.dao.UserDao;
import cz.fi.muni.pa165.calorycounter.backend.dao.UserStatsDao;
import cz.fi.muni.pa165.calorycounter.backend.dao.impl.UserStatsDaoImplJPA.UserStats;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserStatsDto;
import cz.fi.muni.pa165.calorycounter.backend.dto.convert.AuthUserConvert;
import cz.fi.muni.pa165.calorycounter.backend.model.AuthUser;
import cz.fi.muni.pa165.calorycounter.serviceapi.UserService;
import cz.fi.muni.pa165.calorycounter.backend.service.common.DataAccessExceptionNonVoidTemplate;
import cz.fi.muni.pa165.calorycounter.backend.service.common.DataAccessExceptionVoidTemplate;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User service for all operations on UserStats DTO.
 *
 * @author Jan Kucera (Greld)
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    // concrete implementation injected by setter from Spring
    @Autowired
    private UserStatsDao userStatsDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<UserStatsDto> getAllUserStats() {
        return (List<UserStatsDto>) new DataAccessExceptionNonVoidTemplate(null) {
            @Override
            public List<UserStatsDto> doMethod() {
                List<UserStats> daos = userStatsDao.getUsersStats();
                List<UserStatsDto> dtos = new ArrayList<>();
                for (UserStats dao : daos) {
                    UserStatsDto dto = new UserStatsDto();
                    dto.setUserId(dao.getUserId());
                    dto.setNameOfUser(dao.getUserName());
                    dto.setSumBurntCalories(dao.getSumBurntCalories());
                    dto.setSumDuration(dao.getSumDuration());
                    dtos.add(dto);
                }
                return dtos;
            }
        }.tryMethod();
    }

    @Override
    public AuthUserDto login(String username, String password) {
        if (username == null || password == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Invalid username or password in parameter: null");
            log.error("UserServiceImpl.login() called on null parameter: String username or String password", iaex);
            throw iaex;
        }
        return (AuthUserDto) new DataAccessExceptionNonVoidTemplate(username, hash(username, password)) {
            @Override
            public AuthUserDto doMethod() {
                AuthUser entity = userDao.login((String) getU(), (String) getV());
                AuthUserDto dto = AuthUserConvert.fromEntityToDto(entity);
                return dto;
            }
        }.tryMethod();
    }

    @Override
    public Long register(AuthUserDto user, String password) {
        if (user == null || user.getUsername() == null || password == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Invalid user or username or password in parameter: null");
            log.error("UserServiceImpl.register() called on null parameter: AuthUserDto user or String username or String password", iaex);
            throw iaex;
        }
        final AuthUserDto userDto = user;

        AuthUser entity = AuthUserConvert.fromDtoToEntity(user, hash(user.getUsername(), password));
        return (Long) new DataAccessExceptionNonVoidTemplate(entity) {
            @Override
            public Long doMethod() {
                if (userDao.existsUsername(userDto.getUsername())) {
                    throw new IllegalArgumentException("Username is already used.");
                }
                Long entityId = userDao.create((AuthUser) getU());
                return entityId;
            }
        }.tryMethod();
    }

    @Override
    public void update(AuthUserDto dto) {
        if (dto == null || dto.getUserId() == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Cannot update user that"
                    + " doesn't exist. Use create instead.");
            log.error("UserServiceImpl.update() called on non-existent entity", iaex);
            throw iaex;
        } else {
            new DataAccessExceptionVoidTemplate(dto) {
                @Override
                public void doMethod() {
                    AuthUser entity = AuthUserConvert.fromDtoToEntity((AuthUserDto) getU(), null);
                    userDao.update(entity);
                }
            }.tryMethod();
        }
    }

    @Override
    public void remove(AuthUserDto dto) {
        if (dto == null || dto.getUserId() == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Cannot remove user that"
                    + " doesn't exist.");
            log.error("UserServiceImpl.remove() called on non-existent entity", iaex);
            throw iaex;
        } else {
            new DataAccessExceptionVoidTemplate(dto) {
                @Override
                public void doMethod() {
                    userDao.remove(((AuthUserDto) getU()).getUserId());
                }
            }.tryMethod();
        }
    }

    @Override
    public AuthUserDto getByUsername(String username) {
        if (username == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Invalid username in parameter: null");
            log.error("UserServiceImpl.getByUsername() called on null parameter: String username", iaex);
            throw iaex;
        }
        return (AuthUserDto) new DataAccessExceptionNonVoidTemplate(username) {
            @Override
            public AuthUserDto doMethod() {
                AuthUser entity = userDao.getByUsername((String) getU());
                AuthUserDto dto = AuthUserConvert.fromEntityToDto(entity);
                return dto;
            }
        }.tryMethod();
    }

    @Override
    public AuthUserDto getById(Long id) {
        if (id == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Invalid username in parameter: null");
            log.error("UserServiceImpl.getByUsername() called on null parameter: String username", iaex);
            throw iaex;
        }
        return (AuthUserDto) new DataAccessExceptionNonVoidTemplate(id) {
            @Override
            public AuthUserDto doMethod() {
                AuthUser entity = userDao.get((Long) getU());
                AuthUserDto dto = AuthUserConvert.fromEntityToDto(entity);
                return dto;
            }
        }.tryMethod();
    }

    public void setUserStatsDao(UserStatsDao userStatsDao) {
        this.userStatsDao = userStatsDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private String hash(String username, String password) {
        String string = password + "{" + username + "}";
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException ex) {
            log.error(ex.getMessage());
            return string;
        }
        md.update(string.getBytes());

        byte byteData[] = md.digest();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        log.debug("Produced hash: " + sb.toString());
        return sb.toString();
    }

    @Override
    public void setPassword(String username, String password) {
        if (username == null) {
            IllegalArgumentException iaex = new IllegalArgumentException("Invalid username in parameter: null");
            log.error("UserServiceImpl.login() called on null parameter: String username or String password", iaex);
            throw iaex;
        }
        if (password == null || password.isEmpty()) {
            log.info("Password stays unchanged.");
            return;
        }
        new DataAccessExceptionNonVoidTemplate(username, hash(username, password)) {
            @Override
            public Object doMethod() {
                AuthUser user = userDao.getByUsername((String) getU());
                user.setPassword((String) getV());
                userDao.update(user);
                return user;
            }
        }.tryMethod();
    }

    @Override
    public List<AuthUserDto> getAllUsers() {
        List<AuthUser> users = userDao.getAll();
        List<AuthUserDto> result = new LinkedList<>();
        for (AuthUser user : users) {
            result.add(AuthUserConvert.fromEntityToDto(user));
        }
        return result;
    }
}

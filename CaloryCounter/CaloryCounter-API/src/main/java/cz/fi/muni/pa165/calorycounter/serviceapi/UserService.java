package cz.fi.muni.pa165.calorycounter.serviceapi;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserStatsDto;
import java.util.List;

/**
 * User service interface for operations on User DTO.
 *
 * @author Jan Kucera (Greld)
 */
public interface UserService {

    /**
     * Verify if user with given username and password exists.
     *
     * @param username
     * @param password
     * @return User if user with given username and password exist, null
     * otherwise
     */
    AuthUserDto login(String username, String password);

    /**
     * Set a new password to user.
     *
     * @param username
     * @param password
     */
    void setPassword(String username, String password);

    /**
     * Create new user.
     *
     * @param user
     * @param password
     * @return User id if registration was successfull, null otherwise
     */
    Long register(AuthUserDto user, String password);

    /**
     * Update user
     *
     * @param user
     */
    void update(AuthUserDto user);

    /**
     * Remove user
     *
     * @param user
     */
    void remove(AuthUserDto user);

    /**
     * Find user by username
     *
     * @param username
     * @return user with given username.
     */
    AuthUserDto getByUsername(String username);

    /**
     * Find user by id
     *
     * @param id
     * @return user with given id.
     */
    AuthUserDto getById(Long id);

    /**
     * Returns all users with their stats
     *
     * @return All users with their stats
     */
    List<UserStatsDto> getAllUserStats();

    /**
     * Returns all users
     *
     * @return all users
     */
    List<AuthUserDto> getAllUsers();
}

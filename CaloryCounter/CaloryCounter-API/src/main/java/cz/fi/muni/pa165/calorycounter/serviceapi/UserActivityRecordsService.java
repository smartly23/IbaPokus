/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.serviceapi;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserActivityRecordsDto;

/**
 * User service interface for operations on UserActivityRecords DTO.
 *
 * @author Zdenek Lastuvka
 */
public interface UserActivityRecordsService {

    /**
     * Find records of activities for given user
     *
     * @param authUserDto - his activity records returns
     * @return UserActivityRecordsDto
     */
    public UserActivityRecordsDto getAllActivityRecords(AuthUserDto authUserDto);
}

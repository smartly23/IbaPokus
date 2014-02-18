package cz.fi.muni.pa165.calorycounter.frontend;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cz.fi.muni.pa165.calorycounter.serviceapi.UserService;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserStatsDto;
import java.util.List;

/**
 * Stripes ActionBean for handling book operations.
 *
 * @author Kucera
 */
@UrlBinding("/stats/{$event}")
public class StatsActionBean extends BaseActionBean {

    final static Logger log = LoggerFactory.getLogger(StatsActionBean.class);

    @SpringBean //Spring can inject even to private and protected fields
    protected UserService userService;

    //--- part for showing a list of records ----
    private List<UserStatsDto> usersStats;

    public AuthUserDto getUser() {
        return getSessionUser();
    }

    @DefaultHandler
    public Resolution list() {
        log.debug("list()");
        usersStats = userService.getAllUserStats();
        return new ForwardResolution("/stats/list.jsp");
    }

    public List<UserStatsDto> getUsersStats() {
        return usersStats;
    }

}

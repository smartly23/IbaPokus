package cz.fi.muni.pa165.calorycounter.frontend;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserActivityRecordsDto;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import cz.fi.muni.pa165.calorycounter.serviceapi.UserActivityRecordsService;
import cz.fi.muni.pa165.calorycounter.serviceapi.UserService;

/**
 * Stripes ActionBean for handling book operations.
 *
 * @author Lastuvka
 */
@RequireLogin
@UrlBinding("/records/{$event}")
public class RecordsActionBean extends BaseActionBean {

    final static Logger log = LoggerFactory.getLogger(RecordsActionBean.class);

    @SpringBean //Spring can inject even to private and protected fields
    protected UserActivityRecordsService userActivityRecordsService;

    @SpringBean //Spring can inject even to private and protected fields
    protected UserService userService;
    private UserActivityRecordsDto uards;

    @DefaultHandler
    public Resolution list() {
        log.debug("list()");
        uards = userActivityRecordsService.getAllActivityRecords(getSessionUser());
        return new ForwardResolution("/records/list.jsp");

    }

    public UserActivityRecordsDto getUards() {
        return uards;
    }

}

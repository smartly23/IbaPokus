package cz.fi.muni.pa165.calorycounter.frontend;

import cz.fi.muni.pa165.calorycounter.serviceapi.ActivityService;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.ActivityDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.WeightCategory;
import java.util.List;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stripes ActionBean for handling book operations.
 *
 * @author Jan Kučera
 */
@UrlBinding("/activities")
public class ActivitiesActionBean extends BaseActionBean {

    final static Logger log = LoggerFactory.getLogger(ActivitiesActionBean.class);

    @SpringBean //Spring can inject even to private and protected fields
    protected ActivityService activityService;

    private List<ActivityDto> activities;

    private String showDeleted;

    public void setShowDeleted(String value) {
        this.showDeleted = value;
    }

    public String getShowDeleted() {
        return showDeleted;
    }

    public WeightCategory[] getWeightCategories() {
        return WeightCategory.values();
    }

    @DefaultHandler
    public Resolution list() {
        log.debug("list()");
        showDeleted = "false";
        activities = activityService.getActive();
        return new ForwardResolution("/activities/list.jsp");
    }

    @HandlesEvent(value = "switchView")
    public Resolution switchView() {
        log.debug("switchView");
        if (showDeleted == null) {
            showDeleted = "false";
        }
        if (showDeleted.equals("true")) {
            activities = activityService.getDeleted();
        } else {
            activities = activityService.getActive();
        }
        return new ForwardResolution("/activities/list.jsp");
    }

    public List<ActivityDto> getActivities() {
        return activities;
    }

}

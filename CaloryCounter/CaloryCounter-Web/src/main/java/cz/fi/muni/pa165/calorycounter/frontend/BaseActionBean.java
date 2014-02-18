package cz.fi.muni.pa165.calorycounter.frontend;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserRole;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import org.apache.taglibs.standard.functions.Functions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base actionBean implementing the required methods for setting and getting
 * context.
 *
 * @author Martin Kuba makub@ics.muni.cz
 */
public abstract class BaseActionBean implements ActionBean {

    private ActionBeanContext context;
    final static Logger baseLog = LoggerFactory.getLogger(BaseActionBean.class);

    private final UserRole adminRole = UserRole.ADMIN;
    private final UserRole defaultRole = UserRole.USER;

    public UserRole getAdminRole() {
        return adminRole;
    }

    public UserRole getDefaultRole() {
        return defaultRole;
    }

    @Override
    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    @Override
    public ActionBeanContext getContext() {
        return context;
    }

    public static String escapeHTML(String s) {
        return Functions.escapeXml(s);
    }

    protected AuthUserDto getSessionUser() {
        AuthUserDto user = (AuthUserDto) getContext().getRequest().getSession().getAttribute("user");
        baseLog.debug("Session user: " + user);
        return user;
    }

    protected void setSessionUser(AuthUserDto user) {
        getContext().getRequest().getSession().setAttribute("user", user);
    }
}

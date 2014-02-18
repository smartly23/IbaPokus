/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.frontend;

import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserRole;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martin Bryndza
 */
@Intercepts(LifecycleStage.HandlerResolution)
public class SecurityInterceptor implements Interceptor {

    final static Logger log = LoggerFactory.getLogger(AuthenticationActionBean.class);

    @Override
    public Resolution intercept(ExecutionContext ec) throws Exception {
        log.debug("intercept()");
        Resolution resolution = ec.proceed();
        Class currentBean = ec.getActionBean().getClass();
        if (!currentBean.isAnnotationPresent(RequireLogin.class)) {
            return resolution;
        }
        RequireLogin requireLoginAnnotation = (RequireLogin) currentBean.getAnnotation(RequireLogin.class);
        if (isLoggedIn(ec.getActionBeanContext(), requireLoginAnnotation.role())) {
            return resolution;
        } else {
            ActionBeanContext currentContext = ec.getActionBean().getContext();
            currentContext.getRequest().getSession().setAttribute("authPath", ec.getActionBean());
            return new RedirectResolution("/authentication/login.jsp");
        }
    }

    protected boolean isLoggedIn(ActionBeanContext abc, UserRole requiredRole) {
        AuthUserDto user = (AuthUserDto) abc.getRequest().getSession().getAttribute("user");
        if (user == null) {
            log.error("User not authenticated.");
            return false;
        } else if (requiredRole.equals(UserRole.ADMIN) && !user.getRole().equals(UserRole.ADMIN)) {
            log.error("Unsufficient rights.");
            return false;
        } else {
            log.debug("User access granted.");
            return true;
        }
    }

}

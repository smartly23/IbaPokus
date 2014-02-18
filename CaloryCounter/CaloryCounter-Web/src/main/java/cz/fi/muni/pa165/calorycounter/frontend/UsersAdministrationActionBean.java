/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pa165.calorycounter.frontend;

import cz.fi.muni.pa165.calorycounter.serviceapi.UserService;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.AuthUserDto;
import cz.fi.muni.pa165.calorycounter.serviceapi.dto.UserRole;
import java.util.List;
import net.sourceforge.stripes.action.Before;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.integration.spring.SpringBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Martin Bryndza
 */
@RequireLogin(role = UserRole.ADMIN)
@UrlBinding("/admin/users/{$event}/{$user.userId}")
public class UsersAdministrationActionBean extends BaseActionBean {

    final static Logger log = LoggerFactory.getLogger(UsersAdministrationActionBean.class);

    @SpringBean //Spring can inject even to private and protected fields
    protected UserService userService;
    private AuthUserDto user;
    private List<AuthUserDto> users;

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public AuthUserDto getUser() {
        return user;
    }

    public void setUser(AuthUserDto user) {
        this.user = user;
    }

    public List<AuthUserDto> getUsers() {
        return users;
    }

    public void setUsers(List<AuthUserDto> users) {
        this.users = users;
    }

    @Before(stages = LifecycleStage.BindingAndValidation, on = {"delete"})
    public void loadRecordFromDatabase() {
        String id = getContext().getRequest().getParameter("user.userId");
        if (id == null) {
            return;
        }
        user = userService.getById(Long.parseLong(id));
    }

    @DefaultHandler
    public Resolution list() {
        log.debug("list()");
        users = userService.getAllUsers();
        users.remove(0);
        return new ForwardResolution("/administrator/users/list.jsp");
    }

    public Resolution delete() {
        log.debug("delete(): " + user);
        return new ForwardResolution("/administrator/users/delete.jsp");
    }

    public Resolution confirmDelete() {
        log.debug("confirmDelete(): " + user);
        userService.remove(user);
        this.getContext().getMessages().add(new LocalizableMessage("user.delete.success", escapeHTML(user.getName())));
        return new ForwardResolution("/administrator/users/message.jsp");
    }

    public Resolution cancelOperation() {
        log.debug("cancelOperation()");
        return list();
    }

}

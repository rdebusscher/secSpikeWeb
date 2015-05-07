package org.secspike.todo.reminders.view;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.security.SecurityContext;

/**
 *
 */
@Named
@RequestScoped
public class LoginBean {

    @Inject
    private SecurityContext securityContext;

    private String userName;

    private String password;

    public void login() {
        securityContext.login(userName, password);
        securityContext.redirectToKeepURL();
        FacesContext.getCurrentInstance().responseComplete();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

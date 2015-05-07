package org.secspike.todo.auth;

import org.omnifaces.security.jaspic.user.UsernamePasswordIdentityStore;

import javax.enterprise.context.RequestScoped;
import javax.security.SimplePrincipal;
import java.security.Principal;
import java.util.List;

import static java.util.Arrays.asList;

@RequestScoped
public class TestUsernamePasswordIdentityStore implements UsernamePasswordIdentityStore {

    private static final long serialVersionUID = 1L;

    private String userName;
    private String role;

    @Override
    public Principal authenticate(String userName, String password) {
        Principal result = null;
        if (userName.equals(password)) {
            result = new SimplePrincipal(userName);
            this.userName = userName;
            if ("admin".equalsIgnoreCase(userName)) {
                role = "ADMIN";
            } else {
                role = "USER";
            }
        }
        return result;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public List<String> getApplicationRoles() {
        return asList(role);
    }

}

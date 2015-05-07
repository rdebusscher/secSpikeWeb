package javax.security;

import org.omnifaces.security.cdi.Beans;
import org.omnifaces.security.jaspic.core.HttpMsgContext;
import org.omnifaces.security.jaspic.user.UsernamePasswordIdentityStore;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.security.auth.Subject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 *
 */
@RequestScoped
public class SecurityContext {

    public static final String JAVAX_SECURITY_URL = "javax.security.URL";
    public static final String JAVAX_SECURITY_PRINCIPAL = "javax.security.Principal";

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private Principal principal;

    private Principal identityStorePrincipal;

    public boolean login(String userName, String password) {
        boolean result = false;
        UsernamePasswordIdentityStore identityStore = Beans.getReferenceOrNull(UsernamePasswordIdentityStore.class);
        if (identityStore != null) {
            identityStorePrincipal = identityStore.authenticate(userName, password);

            result = identityStorePrincipal != null;
            if (result) {
                HttpMsgContext httpMsgContext = getContext();
                httpMsgContext.notifyContainerAboutLogin(identityStore.getUserName(), identityStore.getApplicationRoles());
            }
        } else {
            try {
                httpServletRequest.login(userName, password);
                result = !"ANONYMOUS".equals(principal.toString()); // TODO Define what the name should be
            } catch (ServletException e) {
                e.printStackTrace();
                // FIXME
            }
        }
        if (result) {
            // TODO this is because we can't use the internals of the Servers to keep this.
            httpServletRequest.getSession().setAttribute(JAVAX_SECURITY_PRINCIPAL, getSubject());
        }
        return result;
    }


    private Subject getSubject() {
        Principal principal = identityStorePrincipal == null ? this.principal : identityStorePrincipal;
        return new Subject(true, new HashSet<>(Arrays.asList(principal)), Collections.<Principal>emptySet(), Collections.<Principal>emptySet());
    }


    public void keepURL(String requestURI) {
        httpServletRequest.getSession().setAttribute(JAVAX_SECURITY_URL, requestURI);

    }

    public void redirectToKeepURL() {
        String requestURI = (String) httpServletRequest.getSession().getAttribute(JAVAX_SECURITY_URL);
        try {
            // TODO @Inject HttpServletResponse is not possible so I assume we are using JSF but this is of course wrong (JAX-RS, MVC)
            HttpServletResponse httpServletResponse = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();

            httpServletResponse.sendRedirect(requestURI);
        } catch (IOException e) {
            // FIXME Probably just put on the signature so that the calling class can handle it.
            e.printStackTrace();
        }
    }

    public boolean isAuthenticated() {
        Subject subject = (Subject) httpServletRequest.getSession().getAttribute(JAVAX_SECURITY_PRINCIPAL);
        // TODO Standardize the ANONYMOUS
        return subject != null && !"ANONYMOUS".equals(subject.getPrincipals().iterator().next().toString());
    }

    public HttpMsgContext getContext() {
        return (HttpMsgContext) httpServletRequest.getAttribute(HttpMsgContext.class.getName());
    }
}

# secSpikeWeb
Security Spec Web version

Based on the ideas of Adam Bien's (and Arjan Tijms - Omnisecurity)

https://github.com/AdamBien/secspike

## Tried

- Custom form authentication with PrimeFaces and JASPIC
- SecurityContext, single place for programmatic login and Principal/Subject

## Status

Not succeeded -> But maybe due to the fact that I can't integrate the required code into the server core.

## Issues
- How can we define the authentication method in web.xml with JASPIC?  
```
    <login-config>
        <auth-method>FORM</auth-method>
        <realm-name>custom</realm-name>
        <form-login-config>
            <form-login-page>/login.xhtml</form-login-page>
            <form-error-page>/error.xhtml</form-error-page>
        </form-login-config>
    </login-config>
```
- How can we define the URL paths which are protected?
```
    <security-constraint>
        <web-resource-collection>
            <web-resource-name>Todos</web-resource-name>
            <url-pattern>/pages/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>USER</role-name>
        </auth-constraint>
    </security-constraint>
```
Now hardcoded in org.omnifaces.security.jaspic.factory.OmniServerAuthContext#validateRequest

- SecurityContext needs to access the JASPIC helper class HttpMsgContext, but is not available there. So I added it to the HttpServletRequest as attribute (see org.secspike.todo.auth.CustomFormAuthModule#initializeModule and javax.security.SecurityContext#getContext)
- Assumed that when the user isn't authenticated the Principal name is **ANONYMOUS** . But this isn't standardized. (see javax.security.SecurityContext#isAuthenticated)


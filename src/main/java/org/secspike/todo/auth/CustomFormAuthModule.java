/*
 * Copyright 2015 OmniFaces.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.secspike.todo.auth;

import org.omnifaces.security.jaspic.core.HttpMsgContext;
import org.omnifaces.security.jaspic.core.HttpServerAuthModule;
import org.secspike.todo.security.CustomFormAuthentication;

import javax.inject.Inject;
import javax.security.SecurityContext;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/**
 * Authentication module that authenticates using Custom form authentication
 *
 * @author Rudy De Busscher
 */
@CustomFormAuthentication
public class CustomFormAuthModule extends HttpServerAuthModule {

    @Inject
    private Principal principal;

    @Inject
    private SecurityContext securityContext;

    @Inject
    private HttpServletRequest httpServletRequest;

    @Override
    public AuthStatus validateHttpRequest(HttpServletRequest request, HttpServletResponse response, HttpMsgContext httpMsgContext) throws AuthException {

        if (httpMsgContext.isProtected()) {
            if (!securityContext.isAuthenticated()) {
                try {
                    securityContext.keepURL(request.getRequestURI());
                    response.sendRedirect(request.getContextPath() + "/login.xhtml");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            return httpMsgContext.doNothing();
        }

        return httpMsgContext.doNothing();
    }


    @Override
    public void initializeModule(HttpMsgContext httpMsgContext) {
        super.initializeModule(httpMsgContext);
        httpServletRequest.setAttribute(HttpMsgContext.class.getName(), httpMsgContext);
    }
}

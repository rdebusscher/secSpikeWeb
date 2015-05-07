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
package org.omnifaces.security.jaspic.authmodules;

import org.omnifaces.security.jaspic.core.HttpMsgContext;
import org.omnifaces.security.jaspic.core.HttpServerAuthModule;
import org.omnifaces.security.jaspic.user.UsernamePasswordIdentityStore;

import javax.annotation.security.BasicAuthentication;
import javax.inject.Inject;
import javax.security.SecurityContext;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static javax.xml.bind.DatatypeConverter.parseBase64Binary;
import static org.omnifaces.security.cdi.Beans.getReferenceOrNull;
import static org.omnifaces.security.jaspic.Utils.isEmpty;

/**
 * Authentication module that authenticates using basic authentication
 * <p/>
 * <p/>
 * Token to username/roles mapping is delegated to an implementation of
 * {@link UsernamePasswordIdentityStore}, which should be registered as CDI
 * bean.
 * <p/>
 * <p/>
 * <b>NOTE:</b> This module makes the simplifying assumption that CDI is
 * available in a SAM. Unfortunately this is not true for every implementation.
 * See https://java.net/jira/browse/JASPIC_SPEC-14
 *
 * @author Arjan Tijms, Rudy De Busscher
 */
@BasicAuthentication
public class BasicAuthModule extends HttpServerAuthModule {

    @Inject
    private SecurityContext securityContext;

    @Override
    public AuthStatus validateHttpRequest(HttpServletRequest request, HttpServletResponse response, HttpMsgContext httpMsgContext) throws AuthException {

        String[] credentials = getCredentials(request);
        if (!isEmpty(credentials)) {

            UsernamePasswordIdentityStore identityStore = getReferenceOrNull(UsernamePasswordIdentityStore.class);
            if (identityStore != null) {
                boolean login = securityContext.login(credentials[0], credentials[1]);
                if (login) {
                    return httpMsgContext.notifyContainerAboutLogin(identityStore.getUserName(), identityStore.getApplicationRoles());
                }
            }
        }

        if (httpMsgContext.isProtected()) {
            response.setHeader("WWW-Authenticate", "Basic realm=\"test realm:\"");
            return httpMsgContext.responseUnAuthorized();
        }

        return httpMsgContext.doNothing();
    }

    private String[] getCredentials(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (!isEmpty(authorizationHeader) && authorizationHeader.startsWith("Basic ")) {
            return new String(parseBase64Binary(authorizationHeader.substring(6))).split(":");
        }

        return null;
    }

}

/*
 * Copyright 2013 OmniFaces.
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
package org.omnifaces.security.jaspic.user;

import java.security.Principal;

/**
 * An authenticator that accepts a basic user name/password combination for authentication.
 *
 * @author Arjan Tijms, Rudy De Busscher
 */
public interface UsernamePasswordIdentityStore extends IdentityStore {

    Principal authenticate(String username, String password);

}

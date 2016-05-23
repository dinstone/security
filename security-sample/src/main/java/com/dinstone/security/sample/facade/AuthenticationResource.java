/*
 * Copyright (C) 2014~2016 dinstone<dinstone@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dinstone.security.sample.facade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.dinstone.security.api.Account;
import com.dinstone.security.api.Authentication;
import com.dinstone.security.api.AuthenticationService;
import com.dinstone.security.spi.DefaultAccount;

@Service
@Path("/authen")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Resource
    private AuthenticationService authenticationService;

    @GET
    @Path("/login")
    public Authentication login(@QueryParam("username") String username, @QueryParam("password") String password,
            @QueryParam("force") boolean force, @Context HttpServletRequest request) {
        Account account = new DefaultAccount(username, password);
        Authentication authen = authenticationService.authenticate(account);
        HttpSession session = request.getSession(true);
        session.setAttribute(Authentication.class.getName(), authen);
        return authen;
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

}

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
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.dinstone.security.model.Authentication;
import com.dinstone.security.service.AuthenticateService;

@Service
@Path("/authen")
@Produces(MediaType.APPLICATION_JSON)
public class AuthenticationResource {

    @Resource
    private AuthenticateService authenticateService;

    @GET
    @Path("/login")
    public Authentication login(@QueryParam("username") String username, @QueryParam("password") String password,
            @QueryParam("force") boolean force, @Context HttpServletRequest request) {
        return null;
    }

}

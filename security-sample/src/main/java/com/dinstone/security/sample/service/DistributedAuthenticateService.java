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

package com.dinstone.security.sample.service;

import javax.annotation.Resource;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.dinstone.security.Configuration;
import com.dinstone.security.model.Authentication;
import com.dinstone.security.service.AuthenticateService;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Service
public class DistributedAuthenticateService implements AuthenticateService {

    @Resource
    private Configuration configuration;

    @Override
    public Authentication checkAuthenToken(String at) {
        if (at == null) {
            return null;
        }

        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
        String uri = configuration.get("uams.authen.token.url");

        Authentication authen = client.target(uri).path(at).request().accept(MediaType.APPLICATION_JSON)
            .get(Authentication.class);
        return authen;
    }

    @Override
    public Authentication checkAuthenTicket(String st) {
        if (st == null) {
            return null;
        }

        Client client = ClientBuilder.newClient().register(JacksonJsonProvider.class);
        String uri = configuration.get("uams.authen.ticket.url");

        Authentication authen = client.target(uri).path(st).request().accept(MediaType.APPLICATION_JSON)
            .get(Authentication.class);
        return authen;
    }

}

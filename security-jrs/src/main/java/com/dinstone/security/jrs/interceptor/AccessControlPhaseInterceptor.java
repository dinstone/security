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

package com.dinstone.security.jrs.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.Session;
import org.apache.cxf.transport.http.AbstractHTTPDestination;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dinstone.security.api.AccessControlException;
import com.dinstone.security.api.AccessControlExceptionType;
import com.dinstone.security.api.Authentication;
import com.dinstone.security.api.AuthenticationService;
import com.dinstone.security.api.AuthorizationService;

/**
 * 认证授权拦截器
 * 
 * @author dinstone
 * @version 1.0.0
 */
public class AccessControlPhaseInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessControlPhaseInterceptor.class);

    private static final String AUTHENTICATION_KEY = Authentication.class.getName();

    private List<String> ignoredOperations = new ArrayList<String>();

    @Resource
    private AuthenticationService authenticationService;

    @Resource
    private AuthorizationService authorizationService;

    public AccessControlPhaseInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        String operation = HttpUtils.getPathToMatch(message, true);
        if (!ignoredOperations.contains(operation)) {
            LOGGER.info("AccessControl intercept a request operation[{}]", operation);
            Session session = message.getExchange().getSession();
            Authentication authentication = (Authentication) session.get(AUTHENTICATION_KEY);
            if (authentication == null) {
                HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
                String authenToken = getCookieValue(request, Authentication.TOKEN);
                authentication = authenticationService.authenticate(authenToken);
                if (authentication == null) {
                    throw new AccessControlException(AccessControlExceptionType.UNAUTHENTICATED);
                } else {
                    session.put(AUTHENTICATION_KEY, authentication);
                }
            }

            authorizationService.authorize(authentication, operation);
        }

    }

    private String getCookieValue(HttpServletRequest request, String name) {
        Cookie cookies[] = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void setIgnoredOperations(List<String> ignoredOperations) {
        if (ignoredOperations != null) {
            this.ignoredOperations.addAll(ignoredOperations);
        }
    }
}

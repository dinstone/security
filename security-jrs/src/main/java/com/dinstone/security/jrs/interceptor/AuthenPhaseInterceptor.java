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

import com.dinstone.security.AccessControlException;
import com.dinstone.security.AccessControlExceptionType;
import com.dinstone.security.Constant;
import com.dinstone.security.model.Authentication;
import com.dinstone.security.service.AuthenticateService;

/**
 * 认证授权拦截器
 * 
 * @author dinstone
 * @version 1.0.0
 */
public class AuthenPhaseInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenPhaseInterceptor.class);

    private List<String> ignoredOperations = new ArrayList<String>();

    @Resource
    private AuthenticateService authenticateService;

    public AuthenPhaseInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        String operation = HttpUtils.getPathToMatch(message, true);
        if (!ignoredOperations.contains(operation)) {
            LOGGER.info("AccessControl intercept a request operation[{}]", operation);
            Session session = message.getExchange().getSession();
            Authentication authentication = (Authentication) session.get(Constant.AUTHEN_KEY_SESSION);
            if (authentication == null) {
                HttpServletRequest request = (HttpServletRequest) message.get(AbstractHTTPDestination.HTTP_REQUEST);
                String authenToken = getCookieValue(request, Constant.AUTHEN_TOKEN_COOKIE);
                authentication = authenticateService.checkAuthenToken(authenToken);
                if (authentication == null) {
                    throw new AccessControlException(AccessControlExceptionType.UNAUTHENTICATED);
                } else {
                    session.put(Constant.AUTHEN_KEY_SESSION, authentication);
                }
            }
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

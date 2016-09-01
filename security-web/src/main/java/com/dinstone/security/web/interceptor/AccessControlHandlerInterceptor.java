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

package com.dinstone.security.web.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.UrlPathHelper;
import org.springframework.web.util.WebUtils;

import com.dinstone.security.api.AccessControlException;
import com.dinstone.security.api.AccessControlExceptionType;
import com.dinstone.security.api.Authentication;
import com.dinstone.security.api.AuthenticationService;
import com.dinstone.security.api.AuthorizationService;
import com.dinstone.security.web.CookieUtil;

public class AccessControlHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessControlHandlerInterceptor.class);

    private static final String AUTHENTICATION_KEY = Authentication.class.getName();

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    private List<String> ignoredOperations = new ArrayList<String>();

    @Resource
    private AuthenticationService authenticationService;

    @Resource
    private AuthorizationService authorizationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String operation = urlPathHelper.getLookupPathForRequest(request);
        if (!ignoredOperations.contains(operation)) {
            LOGGER.info("AccessControl intercept a request operation[{}]", operation);
            Authentication authentication = (Authentication) WebUtils.getSessionAttribute(request, AUTHENTICATION_KEY);
            if (authentication == null) {
                String authenToken = CookieUtil.getCookieValue(request, Authentication.TOKEN);
                authentication = authenticationService.authenticate(authenToken);
                if (authentication == null) {
                    throw new AccessControlException(AccessControlExceptionType.UNAUTHENTICATED);
                } else {
                    WebUtils.setSessionAttribute(request, AUTHENTICATION_KEY, authentication);
                }
            }

            authorizationService.authorize(authentication, operation);
        }

        return true;
    }

    public void setIgnoredOperations(List<String> ignoredOperations) {
        if (ignoredOperations != null) {
            this.ignoredOperations.addAll(ignoredOperations);
        }
    }

    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

}

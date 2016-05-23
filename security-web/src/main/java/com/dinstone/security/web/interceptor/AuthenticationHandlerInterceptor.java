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

public class AuthenticationHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LoggerFactory.getLogger(AuthorizationHandlerInterceptor.class);

    private static final String AUTHENTICATION_KEY = Authentication.class.getName();

    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    private List<String> ignoredOperations = new ArrayList<String>();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String operation = urlPathHelper.getLookupPathForRequest(request);
        logger.info("Authentication intercept a request operation[{}]", operation);

        // ignored operation
        if (!ignoredOperations.contains(operation)) {
            Authentication authentication = (Authentication) WebUtils.getSessionAttribute(request, AUTHENTICATION_KEY);
            if (authentication == null) {
                throw new AccessControlException(AccessControlExceptionType.UNAUTHENTICATED);
            }
        }
        return true;
    }

    public void setIgnoredOperations(List<String> ignoredOperations) {
        if (ignoredOperations != null) {
            this.ignoredOperations.addAll(ignoredOperations);
        }
    }

}

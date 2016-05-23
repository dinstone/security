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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.dinstone.security.ApplicationException;
import com.dinstone.security.BusinessException;
import com.dinstone.security.ExceptionType;
import com.dinstone.security.api.AccessControlException;
import com.dinstone.security.api.AccessControlExceptionType;

public class WebExceptionResolver extends AbstractHandlerExceptionResolver {

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) {
        try {
            if (ex instanceof AccessControlException) {
                ExceptionType et = ((AccessControlException) ex).getExceptionType();
                if (et != null && et == AccessControlExceptionType.DENIED) {
                    response.setHeader("AC-Status", AccessControlExceptionType.DENIED.name());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "access denied");
                } else if (et != null && et == AccessControlExceptionType.UNAUTHENTICATED) {
                    response.setHeader("AC-Status", AccessControlExceptionType.UNAUTHENTICATED.name());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "session invalidation or not logged in");
                } else if (et != null && et == AccessControlExceptionType.UNAUTHORIZED) {
                    response.setHeader("AC-Status", AccessControlExceptionType.UNAUTHORIZED.name());
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "session unauthorized");
                } else {
                    response.setHeader("AC-Status", AccessControlExceptionType.FORBIDDEN.name());
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "access forbidden");
                }
                return new ModelAndView();
            } else if (ex instanceof BusinessException) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
            } else if (ex instanceof ApplicationException) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ex.getMessage());
                return new ModelAndView();
            }
        } catch (Exception e) {
            logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", e);
        }
        return null;
    }
}

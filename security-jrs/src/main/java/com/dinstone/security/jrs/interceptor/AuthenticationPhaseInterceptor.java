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

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.jaxrs.utils.HttpUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dinstone.security.api.AccessControlException;
import com.dinstone.security.api.AccessControlExceptionType;
import com.dinstone.security.api.Authentication;

/**
 * 认证拦截器
 * 
 * @author dinstone
 * @version 1.0.0
 */
public class AuthenticationPhaseInterceptor extends AbstractPhaseInterceptor<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationPhaseInterceptor.class);

    private static final String AUTHENTICATION_KEY = Authentication.class.getName();

    private List<String> ignoredOperations = new ArrayList<String>();

    public AuthenticationPhaseInterceptor() {
        super(Phase.RECEIVE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        String operation = HttpUtils.getPathToMatch(message, true);
        LOG.info("Authentication intercept a request operation[{}]", operation);

        // ignored operation
        if (!ignoredOperations.contains(operation)) {
            Session session = message.getExchange().getSession();
            Authentication authentication = (Authentication) session.get(AUTHENTICATION_KEY);
            if (authentication == null) {
                throw new AccessControlException(AccessControlExceptionType.UNAUTHENTICATED);
            }
        }

    }

    public void setIgnoredOperations(List<String> ignoredOperations) {
        if (ignoredOperations != null) {
            this.ignoredOperations.addAll(ignoredOperations);
        }
    }
}

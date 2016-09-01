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

package com.dinstone.security.web.tag;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.dinstone.security.api.AccessControlException;
import com.dinstone.security.api.Authentication;
import com.dinstone.security.api.AuthenticationService;
import com.dinstone.security.api.AuthorizationService;
import com.dinstone.security.web.CookieUtil;

public class AccessControlTag extends TagSupport {

    /** serialVersionUID = 1L */
    private static final long serialVersionUID = 1L;

    private String operation;

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doEndTag()
     */
    @Override
    public int doEndTag() throws JspException {
        return Tag.EVAL_PAGE;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.servlet.jsp.tagext.TagSupport#doStartTag()
     */
    @Override
    public int doStartTag() throws JspException {
        ServletContext servletContext = pageContext.getServletContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String authenToken = CookieUtil.getCookieValue(request, Authentication.TOKEN);
        if (authenToken == null) {
            return Tag.SKIP_BODY;
        }

        WebApplicationContext wac = RequestContextUtils.getWebApplicationContext(request, servletContext);
        AuthenticationService authenticationService = wac.getBean(AuthenticationService.class);
        AuthorizationService authorizationService = wac.getBean(AuthorizationService.class);
        try {
            Authentication authen = authenticationService.authenticate(authenToken);
            authorizationService.authorize(authen, operation);
        } catch (AccessControlException e) {
            return Tag.SKIP_BODY;
        }

        return Tag.EVAL_BODY_INCLUDE;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

}

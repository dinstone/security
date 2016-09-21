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

package com.dinstone.security.sample.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.dinstone.security.Configuration;
import com.dinstone.security.Constant;
import com.dinstone.security.model.Authentication;
import com.dinstone.security.sample.service.DistributedAuthenticateService;

@Service
@RequestMapping("/authen")
public class AuthenController {

    private static final Logger LOG = LoggerFactory.getLogger(AuthenController.class);

    @Resource
    private DistributedAuthenticateService accessControlService;

    @Resource
    private Configuration configuration;

    /**
     * 登录
     * 
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/callback")
    public ModelAndView callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String atp = request.getParameter(Constant.AUTHEN_TICKET_PARAM);
        if (atp != null && !atp.isEmpty()) {
            LOG.info("callback with atp={}", atp);
            Authentication authen = accessControlService.checkAuthenTicket(atp);
            if (authen != null) {
                HttpSession session = request.getSession(true);
                session.setAttribute(Constant.AUTHEN_KEY_SESSION, authen);
                Cookie cookie = new Cookie(Constant.AUTHEN_TOKEN_COOKIE, authen.getAuthenToken());
                cookie.setPath("/");
                cookie.setMaxAge(30 * 60);
                response.addCookie(cookie);

                session.setAttribute("authen", authen);
            }
        }

        return new ModelAndView("forward:/authen/check.do");
    }

    @RequestMapping(value = "/check")
    public ModelAndView check(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(true);
        // String referer = request.getHeader("Referer");
        // LOG.info("Referer:" + referer);
        Authentication authen = (Authentication) session.getAttribute(Constant.AUTHEN_KEY_SESSION);
        if (authen == null) {
            String authenUrl = configuration.get("uams.authen.login.url");
            // authenView =
            // "http://192.168.1.33:8080/uams/authen/login.do?scu=http://localhost:8080/sample/authen/callback.do";
            return new ModelAndView("redirect:" + authenUrl);
        }

        String viewName = configuration.get("authen.check.ok");
        return new ModelAndView(viewName);
    }

    public static String getCookieValue(HttpServletRequest request, String name) {
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

}

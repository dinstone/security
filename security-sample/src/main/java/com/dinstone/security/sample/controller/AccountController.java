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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.WebUtils;

import com.dinstone.security.BusinessException;
import com.dinstone.security.api.Authentication;
import com.dinstone.security.api.AuthenticationService;
import com.dinstone.security.spi.DefaultAccount;

@Controller
@RequestMapping("/account")
public class AccountController {

    private static final Logger LOG = LoggerFactory.getLogger(AccountController.class);

    @Resource
    private AuthenticationService authenticationService;

    @RequestMapping(value = "/login")
    @ResponseBody
    public Map<String, Object> accountLogin(String username, String password, HttpServletRequest request) {
        String msg = "登陆成功";
        try {
            LOG.info("username = " + username);
            LOG.info("password = " + password);

            DefaultAccount account = new DefaultAccount(username, password);
            Authentication authen = authenticationService.authenticate(account);
            WebUtils.setSessionAttribute(request, Authentication.class.getName(), authen);
        } catch (BusinessException se) {
            msg = "无效的用户名或密码";
            System.out.println(msg);
        }
        Map<String, Object> mod = new HashMap<String, Object>();
        mod.put("msg", msg);
        return mod;
    }

    private void checkUsernamePassword(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            throw new BusinessException(AccountExceptionType.USERNAME_NULL);
        }
        if (StringUtils.isEmpty(password)) {
            throw new BusinessException(AccountExceptionType.PASSWORD_NULL);
        }
    }

    @RequestMapping(value = "/info")
    @ResponseBody
    public Object info(String username) {
        List<String> list = new ArrayList<String>();
        list.add("电视");
        list.add("洗衣机");
        list.add("冰箱");
        list.add("电脑");
        list.add("汽车");
        list.add("空调");
        list.add("自行车");
        list.add("饮水机");
        list.add("热水器");
        return list;
    }
}

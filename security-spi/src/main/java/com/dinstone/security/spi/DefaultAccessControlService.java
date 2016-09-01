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

package com.dinstone.security.spi;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.dinstone.security.api.AbstractAccessControlService;
import com.dinstone.security.api.AccessControlException;
import com.dinstone.security.api.AccessControlExceptionType;
import com.dinstone.security.api.Account;
import com.dinstone.security.api.Authentication;
import com.dinstone.security.api.Permission;
import com.dinstone.security.api.Subject;
import com.dinstone.security.dao.AccessControlDao;

@Service
public class DefaultAccessControlService extends AbstractAccessControlService {

    @Resource
    private AccessControlDao accessControlDao;

    @Override
    public Authentication authenticate(String authenToken) {
        Authentication authen = accessControlDao.findAuthentication(authenToken);
        Date now = new Date();
        if (authen == null || now.getTime() > authen.getExpiration()) {
            AccessControlException acException = new AccessControlException(AccessControlExceptionType.UNAUTHENTICATED);
            acException.setProperty("authenToken", authenToken);
            throw acException;
        }
        return authen;
    }

    @Override
    protected Authentication createAuthentication(Account account) {
        String principal = ((DefaultAccount) account).getPrincipal();

        Subject subject = accessControlDao.findSubject(principal);
        Set<Permission> permissions = accessControlDao.findPermissions(principal);

        DefaultAuthentication authentication = new DefaultAuthentication();
        authentication.setToken(principal);
        authentication.setPrincipal(principal);
        authentication.setSubject(subject);
        authentication.setPermissions(permissions);

        return authentication;
    }

    @Override
    protected Permission findPermission(Object operation) {
        return accessControlDao.findPermission((String) operation);
    }

    @Override
    protected boolean checkAccount(Account account) {
        DefaultAccount upAccount = (DefaultAccount) account;
        DefaultAccount userAccount = accessControlDao.findAccount(upAccount.getPrincipal());
        if (userAccount == null) {
            return false;
        }

        String actual = upAccount.getCredential();
        String expect = userAccount.getPassword();
        if (actual == expect) {
            return true;
        }

        if (actual != null && actual.equals(expect)) {
            return true;
        }

        return false;
    }

    public void setAccessControlDao(AccessControlDao accessControlDao) {
        this.accessControlDao = accessControlDao;
    }

}

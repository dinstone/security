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

package com.dinstone.security.sample.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.dinstone.security.api.Permission;
import com.dinstone.security.api.Subject;
import com.dinstone.security.dao.AccessControlDao;
import com.dinstone.security.spi.DefaultAccount;
import com.dinstone.security.spi.DefaultAuthentication;
import com.dinstone.security.spi.DefaultSubject;

@Service("accessControlDao")
public class MemoryAccessControlDao implements AccessControlDao {

    private Map<String, DefaultAuthentication> authenMap = new HashMap<String, DefaultAuthentication>();

    public MemoryAccessControlDao() {
        DefaultAuthentication authen = new DefaultAuthentication();
        authen.setPrincipal("dinstone");
        authenMap.put("dinstone", authen);
    }

    @Override
    public DefaultAccount findAccount(String principal) {
        if ("dinstone".equals(principal)) {
            return new DefaultAccount(principal, "123456");
        }
        return null;
    }

    @Override
    public Subject findSubject(String principal) {
        if ("dinstone".equals(principal)) {
            DefaultSubject subject = new DefaultSubject();
            subject.setId(principal);
            subject.setName("郭金飞");
            return subject;
        }

        return null;
    }

    @Override
    public Set<Permission> findPermissions(String principal) {
        return null;
    }

    @Override
    public Permission findPermission(String operation) {
        return null;
    }

}

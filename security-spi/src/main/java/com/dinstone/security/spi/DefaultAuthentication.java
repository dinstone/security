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

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.dinstone.security.api.Authentication;
import com.dinstone.security.api.Permission;
import com.dinstone.security.api.Subject;

public class DefaultAuthentication implements Authentication, Serializable {

    /** serialVersionUID = 1L */
    private static final long serialVersionUID = 1L;

    /** 当事人标识信息 */
    private String principal;

    /** 当事人主体信息 */
    private Subject subject;

    private String token;

    private int expiration;

    private Set<Permission> permissions = new HashSet<Permission>();

    public DefaultAuthentication() {
        super();
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    @Override
    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        if (permissions != null) {
            this.permissions.addAll(permissions);
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getExpiration() {
        return expiration;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

}

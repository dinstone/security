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

import com.dinstone.security.api.Account;

public class DefaultAccount implements Account, Serializable {

    /** serialVersionUID = 1L */
    private static final long serialVersionUID = 1L;

    private String username;

    private String password;

    public DefaultAccount() {
        super();
    }

    public DefaultAccount(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String getPrincipal() {
        return username;
    }

    @Override
    public String getCredential() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}

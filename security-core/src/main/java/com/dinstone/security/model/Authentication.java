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

package com.dinstone.security.model;

import java.io.Serializable;

/**
 * 认证信息。通常会存储于远程缓存中，如果当前回话没有认证信息，则会从远程服务获取，同时可能会在本地缓存一份。
 * 
 * @author dinstone
 * @version 1.0.0
 */
public class Authentication implements Serializable {

    /**  */
    private static final long serialVersionUID = 1L;

    /** 账号标识 */
    private String accountId;

    /** 账号类型,本地注册或第三方登录账号 */
    private String accountType;

    /** 账号主体,账号关联的用户信息 */
    private Subject subject;

    /** 有效期截止时刻，为毫秒表示 */
    private long expiration;

    /** 认证标识 */
    private String authenToken;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getAuthenToken() {
        return authenToken;
    }

    public void setAuthenToken(String authenToken) {
        this.authenToken = authenToken;
    }

    @Override
    public String toString() {
        return "Authentication [accountId=" + accountId + ", accountType=" + accountType + ", subject=" + subject
                + ", expiration=" + expiration + ", authenToken=" + authenToken + "]";
    }

}

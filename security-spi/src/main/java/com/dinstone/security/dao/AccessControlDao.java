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

package com.dinstone.security.dao;

import java.util.Set;

import com.dinstone.security.api.Authentication;
import com.dinstone.security.api.Permission;
import com.dinstone.security.api.Subject;
import com.dinstone.security.spi.DefaultAccount;

public interface AccessControlDao {

    DefaultAccount findAccount(String principal);

    Subject findSubject(String principal);

    Set<Permission> findPermissions(String principal);

    Permission findPermission(String operation);

    Authentication findAuthentication(String authenToken);

}

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

package com.dinstone.security.api;

import java.util.Set;

import com.dinstone.security.BusinessException;

/**
 * 抽象实现
 * 
 * @author dinstone
 * @version 1.0.0
 */
public abstract class AbstractAccessControlService implements AuthenticationService, AuthorizationService {

    @Override
    public Authentication authenticate(Account account) {
        // check account
        if (!checkAccount(account)) {
            throw new BusinessException(AccessControlExceptionType.INVALID_ACCOUNT).setProperty("account", account);
        }

        Authentication authentication = createAuthentication(account);

        return authentication;
    }

    @Override
    public void authorize(Authentication authentication, Object operation) {
        if (operation == null) {
            return;
        }

        Permission expectPermission = findPermission(operation);
        if (expectPermission == null) {
            return;
        }

        if (authentication != null) {
            Set<Permission> hadPermissions = authentication.getPermissions();
            for (Permission permission : hadPermissions) {
                if (permission.implies(expectPermission)) {
                    return;
                }
            }

            AccessControlException acException = new AccessControlException(AccessControlExceptionType.UNAUTHORIZED);
            acException.setProperty("authentication", authentication);
            acException.setProperty("operation", operation);
            throw acException;
        } else {
            AccessControlException acException = new AccessControlException(AccessControlExceptionType.UNAUTHENTICATED);
            acException.setProperty("authentication", authentication);
            acException.setProperty("operation", operation);
            throw acException;
        }
    }

    protected abstract boolean checkAccount(Account account);

    /**
     * create authentication object.
     *
     * @param account
     * @return
     */
    protected abstract Authentication createAuthentication(Account account);

    /**
     * check operation's permission.
     *
     * @param operation
     * @return
     */
    protected abstract Permission findPermission(Object operation);

}

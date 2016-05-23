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

package com.dinstone.security;

/**
 * BusinessException表示违法业务规则导致的异常，需要调用者根据异常类型来处理；
 *
 * @author guojinfei
 * @version 1.0.0
 */
public class BusinessException extends ApplicationException {

    /**  */
    private static final long serialVersionUID = 1L;

    public BusinessException(ExceptionType errorType) {
        super(errorType);
    }

    public BusinessException(ExceptionType errorType, Throwable cause) {
        super(errorType, cause);
    }

    public BusinessException(ExceptionType exceptionType, String message, Throwable cause) {
        super(exceptionType, message, cause);
    }

    public BusinessException(ExceptionType exceptionType, String message) {
        super(exceptionType, message);
    }

}

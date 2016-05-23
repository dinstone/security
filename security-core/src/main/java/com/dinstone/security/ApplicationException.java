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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * ApplicationException表示应用未按预期执行导致的异常，需要应用框架根据异常类型来处理；
 *
 * @author guojinfei
 * @version 1.0.0
 */
public class ApplicationException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final Map<String, Object> properties = new HashMap<String, Object>();

    private ExceptionType exceptionType;

    public ApplicationException(ExceptionType exceptionType) {
        this(exceptionType, exceptionType != null ? exceptionType.toString() : null, null);
    }

    public ApplicationException(ExceptionType exceptionType, String message) {
        this(exceptionType, message, null);
    }

    public ApplicationException(ExceptionType exceptionType, Throwable cause) {
        this(exceptionType, exceptionType != null ? exceptionType.toString() : null, cause);
    }

    public ApplicationException(ExceptionType exceptionType, String message, Throwable cause) {
        super(message, cause);
        this.exceptionType = exceptionType;
    }

    /**
     * the exceptionType to get
     *
     * @return the exceptionType
     * @see ApplicationException#exceptionType
     */
    public ExceptionType getExceptionType() {
        return exceptionType;
    }

    public Object getProperty(String name) {
        return properties.get(name);
    }

    public ApplicationException setProperty(String name, Object value) {
        properties.put(name, value);
        return this;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        synchronized (s) {
            printStackTrace(new PrintWriter(s));
        }
    }

    @Override
    public void printStackTrace(PrintWriter s) {
        synchronized (s) {
            s.println(this);

            if (exceptionType != null) {
                s.print("\tep ");
                s.print(exceptionType.getClass().getName() + "." + exceptionType + " @ ");
                s.println(properties.toString());
            }

            StackTraceElement[] trace = getStackTrace();
            for (int i = 0; i < trace.length; i++) {
                s.println("\tat " + trace[i]);
            }

            Throwable ourCause = getCause();
            if (ourCause != null) {
                ourCause.printStackTrace(s);
            }
            s.flush();
        }
    }

    public static ApplicationException wrap(Throwable exception) {
        return wrap(null, exception);
    }

    public static ApplicationException wrap(ExceptionType exceptionType, Throwable exception) {
        if (exception instanceof ApplicationException) {
            ApplicationException ae = (ApplicationException) exception;
            if (exceptionType != null && exceptionType != ae.getExceptionType()) {
                return new ApplicationException(exceptionType, exception);
            }
            return ae;
        } else {
            return new ApplicationException(exceptionType, exception);
        }
    }

}

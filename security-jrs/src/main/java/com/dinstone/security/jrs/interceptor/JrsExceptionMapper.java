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

package com.dinstone.security.jrs.interceptor;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.HashMap;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.ExceptionMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dinstone.security.AccessControlException;
import com.dinstone.security.AccessControlExceptionType;
import com.dinstone.security.ApplicationException;
import com.dinstone.security.BusinessException;
import com.dinstone.security.ExceptionType;

/**
 * @author dinstone
 * @version 1.0.0
 */
public class JrsExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = LoggerFactory.getLogger(JrsExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        LOG.debug("SystemExceptionMapper handle exception", exception);

        if (exception instanceof WebApplicationException) {
            return covertWebApplicationException((WebApplicationException) exception);
        } else if (exception instanceof ApplicationException) {
            return covertApplicationException((ApplicationException) exception);
        } else {
            return covertUnkownException(exception);
        }
    }

    private Response covertApplicationException(ApplicationException exception) {
        Status status = Status.SERVICE_UNAVAILABLE;
        if (exception instanceof BusinessException) {
            status = Status.BAD_REQUEST;
        } else if (exception instanceof AccessControlException) {
            ExceptionType et = exception.getExceptionType();
            if (et == AccessControlExceptionType.FORBIDDEN) {
                status = Status.FORBIDDEN;
            } else if (et == AccessControlExceptionType.UNAUTHORIZED) {
                status = Status.FORBIDDEN;
            } else if (et == AccessControlExceptionType.UNAUTHENTICATED) {
                status = Status.UNAUTHORIZED;
            }
        }

        int code = status.getStatusCode();
        String message = exception.getMessage();
        if (message == null) {
            Throwable cause = exception.getCause();
            if (cause != null) {
                message = cause.getMessage();
            }
        }

        HashMap<String, String> errorMessage = new HashMap<String, String>();
        errorMessage.put("code", String.valueOf(code));
        errorMessage.put("message", message);

        return Response.status(status).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
    }

    private Response covertWebApplicationException(WebApplicationException exception) {
        Response rep = exception.getResponse();
        if (rep == null) {
            rep = Response.serverError().build();
        }

        StatusType statusType = rep.getStatusInfo();
        int code = statusType.getStatusCode();
        String message = exception.getMessage();
        if (message == null) {
            Throwable cause = exception.getCause();
            if (cause != null) {
                message = cause.getMessage();
            }
        }

        HashMap<String, String> errorMessage = new HashMap<String, String>();
        errorMessage.put("code", String.valueOf(code));
        errorMessage.put("message", message);

        return Response.status(statusType).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
    }

    private Response covertUnkownException(Exception exception) {
        Status status = Status.INTERNAL_SERVER_ERROR;

        int code = status.getStatusCode();
        String message = exception.getMessage();
        if (message == null) {
            Throwable cause = exception.getCause();
            if (cause != null) {
                message = cause.getMessage();
            }
        }

        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintStream(byteArray));
        String trace = byteArray.toString();

        HashMap<String, String> errorMessage = new HashMap<String, String>();
        errorMessage.put("code", String.valueOf(code));
        errorMessage.put("message", message);
        errorMessage.put("trace", trace);

        return Response.status(status).entity(errorMessage).type(MediaType.APPLICATION_JSON).build();
    }

}

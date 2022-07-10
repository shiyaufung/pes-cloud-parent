/*
 * Copyright 2017-2022 shiyoufeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.piperplatform.pes.commons.web.filter;

import cn.dev33.satoken.exception.*;
import cn.hutool.core.exceptions.ExceptionUtil;
import com.piperplatform.pes.commons.entity.BaseResponse;
import com.piperplatform.pes.commons.enums.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.io.IOException;

/**
 * Global exception capture
 *
 * @author shiyoufeng
 * @date 2021/3/31
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionFilter {
    /**
     * The operation triggered before each method of the current class enters
     *
     * @param request
     * @throws IOException
     */
    @ModelAttribute
    public void get(HttpServletRequest request) throws IOException {

    }

    /**
     * Account not logged in exception handling
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    public BaseResponse<?> handlerNotLoginException(NotLoginException e) {
        HttpStatus httpStatus;
        switch (e.getType()) {
            // No token provided. Token is invalid
            case NotLoginException.NOT_TOKEN:
            case NotLoginException.INVALID_TOKEN:
                // User account is not logged in
                httpStatus = HttpStatus.A0301;
                break;
            // Token has been pushed offline
            case NotLoginException.BE_REPLACED:
                // The current account has been logged in elsewhere
                httpStatus = HttpStatus.A0305;
                break;
            // The token has expired, and the token has been kicked off the line
            case NotLoginException.TOKEN_TIMEOUT:
            case NotLoginException.KICK_OUT:
                // User login has expired
                httpStatus = HttpStatus.A0230;
                break;
            default:
                // User login exception
                httpStatus = HttpStatus.A0200;
                break;
        }
        return BaseResponse.error(httpStatus);
    }

    /**
     * Role verification exception handling
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NotRoleException.class)
    @ResponseBody
    public BaseResponse<?> handlerNotRoleException(NotRoleException e) {
        // Unauthorized access
        return BaseResponse.error(HttpStatus.A0301);
    }

    /**
     * Exception handling of permission verification
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public BaseResponse<?> handlerNotPermissionException(NotPermissionException e) {
        // Unauthorized access
        return BaseResponse.error(HttpStatus.A0301);
    }

    /**
     * Account disabling exception handling
     *
     * @param e
     * @return
     */
    @ExceptionHandler(DisableLoginException.class)
    @ResponseBody
    public BaseResponse<?> handlerDisableLoginException(DisableLoginException e) {
        // User account is frozen
        return BaseResponse.error(HttpStatus.A0202, e.getDisableTime());
    }

    /**
     * Exception handling of account secondary authentication failure
     *
     * @param e
     * @return
     */
    @ExceptionHandler(NotSafeException.class)
    @ResponseBody
    public BaseResponse<?> handlerNotSafeException(NotSafeException e) {
        // User authorization secondary authentication failed
        return BaseResponse.error(HttpStatus.A0304);
    }

    /**
     * Abnormal parameter verification
     *
     * @param e
     * @return Return data
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public BaseResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        return BaseResponse.error(HttpStatus.A0400);
    }

    /**
     * Validation exception
     *
     * @param e
     * @return Return data
     */
    @ExceptionHandler(ValidationException.class)
    @ResponseBody
    public BaseResponse<?> handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e);
        return BaseResponse.error(HttpStatus.A0400);
    }

    /**
     * Constraint violation exception
     *
     * @param e
     * @return Return data
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public BaseResponse<?> handleConstraintViolationException(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        return BaseResponse.error(HttpStatus.A0400);
    }

    /**
     * Global general exception
     *
     * @param e
     * @return Return data
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public BaseResponse<?> handleException(Exception e) {
        log.error(ExceptionUtil.stacktraceToString(e));
        return BaseResponse.error(HttpStatus.A0500);
    }
}

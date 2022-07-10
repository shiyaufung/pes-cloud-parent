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
package com.piperplatform.pes.commons.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.piperplatform.pes.commons.enums.HttpStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author shiyoufeng
 * @date 2022/4/21
 */
@Data
@Schema(description = "Data response object")
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 6320550141346709445L;

    @Schema(description = "code")
    private String code;

    @Schema(description = "message")
    private String msg;

    @Schema(description = "data")
    private T data;


    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    public static <T> BaseResponse<T> success(T data) {
        return BaseResponse(HttpStatus.SUCCESS, data);
    }

    public static <T> BaseResponse<T> error() {
        return error(HttpStatus.ERROR, null);
    }

    public static <T> BaseResponse<T> error(HttpStatus httpStatus) {
        return error(httpStatus, httpStatus.getDescription());
    }

    public static <T> BaseResponse<T> error(HttpStatus httpStatus, T data) {
        return BaseResponse(httpStatus, data);
    }

    public static <T> BaseResponse<T> error(HttpStatus httpStatus, String msg) {
        return BaseResponse(httpStatus.getCode(), msg, null);
    }

    /**
     * Default status information export BaseResponse
     *
     * @param httpStatus 状态信息
     * @param data       携带数据
     * @param <T>        携带数据泛型
     * @return 返回信息
     */
    private static <T> BaseResponse<T> BaseResponse(HttpStatus httpStatus, T data) {
        return BaseResponse(httpStatus.getCode(), httpStatus.getDescription(), data);
    }

    /**
     * Export BaseResponse
     *
     * @param code 状态码
     * @param msg  状态信息
     * @param data 携带数据
     * @param <T>  携带数据泛型
     * @return 返回信息
     */
    private static <T> BaseResponse<T> BaseResponse(String code, String msg, T data) {
        BaseResponse<T> result = new BaseResponse<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    @JsonIgnore
    public boolean isSuccess() {
        return HttpStatus.SUCCESS.is(code);
    }
}

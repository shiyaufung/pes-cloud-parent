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
package com.piperplatform.pes.commons.web.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * LogDTO
 *
 * @author Piper
 * @since 2021-08-17
 */
@Data
@Accessors(chain = true)
public class LogDTO {

    private String id;
    /**
     * timeStamp
     */
    private Long timeStamp;

    /**
     * level
     */
    private String level;

    /**
     * threadName
     */
    private String threadName;

    /**
     * message
     */
    private String message;

    /**
     * className
     */
    private String className;

    /**
     * fileName
     */
    private String fileName;

    /**
     * methodName
     */
    private String methodName;

    /**
     * lineNumber
     */
    private Integer lineNumber;
}

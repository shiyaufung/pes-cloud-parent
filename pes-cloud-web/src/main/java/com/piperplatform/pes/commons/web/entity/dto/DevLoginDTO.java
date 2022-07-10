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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author shiyoufeng
 * @date 2022/6/5
 */
@Data
@Schema(description="")
public class DevLoginDTO {

    @Schema(description = "devUsername")
    private String devUsername;

    @Schema(description="devPassword")
    private String devPassword;

    @Schema(description="testUsername")
    private String testUsername;

    @Schema(description="rememberMe")
    private Boolean rememberMe;

    @Schema(description="captcha")
    private String captcha;
}

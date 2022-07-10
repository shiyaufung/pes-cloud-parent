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

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author youfeng.shi
 * @date 2022/5/5
 */
@Data
public class UserInfoDTO implements Serializable {

    private static final long serialVersionUID = 6507318980931791094L;

    private Long id;

    private String empId;

    private String empName;

    private String empUid;

    private String empDisName;

    private Integer empGender;

    private String dutyName;

    private Map<String,DeptDto> permissions;

    private Set<String> roles;
}

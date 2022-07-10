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

/**
 * @author shiyoufeng
 * @date 2022/6/27
 */
@Data
public class DeptDto implements Serializable {

    private static final long serialVersionUID = -463634096592526960L;

    private String deptName;

    private String deptCode;

    private String deptTree;

    private String company;

    private String department;

    private String section;

    private String region;

    private String sec;

    private String team;

    private String teamShift;
}

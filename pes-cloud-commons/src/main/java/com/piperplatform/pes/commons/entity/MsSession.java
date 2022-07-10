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

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author shiyoufeng
 * @date 2022/4/22
 */
@Data
@Accessors(chain = true)
public class MsSession implements Serializable {

    private static final long serialVersionUID = -2723589291150410705L;

    private String host;
    private String port;
    private String appName;
    private String interfaceName;

    private String sc;
    private Long id;
    private String token;

    private String empId;
    private String name;

}

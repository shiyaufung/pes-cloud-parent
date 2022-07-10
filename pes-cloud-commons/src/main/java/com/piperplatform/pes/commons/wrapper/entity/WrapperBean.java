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
package com.piperplatform.pes.commons.wrapper.entity;

import com.piperplatform.pes.commons.annotation.MsLabelField;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author youfeng.shi
 * @date 2022/5/5
 */
@Data
public class WrapperBean {

    @MsLabelField("id")
    private String id;

    @MsLabelField("name")
    private String name;

    @MsLabelField("age")
    private String age;

}

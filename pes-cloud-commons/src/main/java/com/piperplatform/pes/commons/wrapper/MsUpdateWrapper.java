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
package com.piperplatform.pes.commons.wrapper;

import cn.hutool.core.util.StrUtil;
import com.piperplatform.pes.commons.annotation.MsLabelField;
import com.piperplatform.pes.commons.entity.DynamicField;
import com.piperplatform.pes.commons.entity.RdbmsDynamicFieldUpdateRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author youfeng.shi
 * @date 2022/5/4
 */
@Data
@Slf4j
public class MsUpdateWrapper<T> implements Serializable {

    private static final long serialVersionUID = -4983147638148447733L;

    private RdbmsDynamicFieldUpdateRequest request;

    public MsUpdateWrapper() {
        request = new RdbmsDynamicFieldUpdateRequest();
        request.setContents(new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    public RdbmsDynamicFieldUpdateRequest warp(T t) throws IllegalAccessException {
        DynamicField dynamicField = new DynamicField();
        HashMap<String, Object> data = new HashMap<>();

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            boolean bool = field.isAnnotationPresent(MsLabelField.class);
            if (bool) {
                String key = field.getAnnotation(MsLabelField.class).value().toUpperCase();
                field.setAccessible(true);
                Object o = field.get(t);
                if (o == null) {
                    continue;
                }
                if (o instanceof List) {
                    o = StrUtil.join(",", (List) o);
                }

                if ("ID".equals(key)) {
                    dynamicField.setId(o.toString());
                } else {
                    data.put(key, o);
                }
            }
        }
        if (StrUtil.isEmpty(dynamicField.getId())) {
            request.getContents().clear();
            throw new IllegalAccessException(String.format("dynamicField not has main key id please check:[%s]", t));
        }
        dynamicField.setData(data);
        request.getContents().add(dynamicField);
        return request;
    }

    public RdbmsDynamicFieldUpdateRequest warp(List<T> list) throws IllegalAccessException {
        for (T t : list) {
            warp(t);
        }
        return request;
    }

}

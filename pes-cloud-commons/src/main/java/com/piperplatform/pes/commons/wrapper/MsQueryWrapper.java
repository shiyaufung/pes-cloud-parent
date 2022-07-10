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
import com.piperplatform.pes.commons.entity.BaseResponse;
import com.piperplatform.pes.commons.entity.MsQueryCondition;
import com.piperplatform.pes.commons.entity.RdbmsDynamicFieldQueryRequest;
import com.piperplatform.pes.commons.entity.RdbmsDynamicFieldQueryResponse;
import com.piperplatform.pes.commons.enums.QueryKeyword;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author youfeng.shi
 * @date 2022/5/4
 */
@Data
@Slf4j
public class MsQueryWrapper<T> implements Serializable {

    private RdbmsDynamicFieldQueryRequest request;
    private Class<T> clazz;

    public MsQueryWrapper() {
        request = new RdbmsDynamicFieldQueryRequest();
        request.setConditions(new ArrayList<>());
    }

    @SuppressWarnings("unchecked")
    public MsQueryWrapper(Class<T> clazz) {
        this();
        this.clazz = clazz;
        request.setLabels(new ArrayList<>());
    }

    public RdbmsDynamicFieldQueryRequest warp() {
        if (request.getLabels() != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                boolean bool = field.isAnnotationPresent(MsLabelField.class);
                if (bool) {
                    String key = field.getAnnotation(MsLabelField.class).value().toUpperCase();
                    if ("ID".equals(key)) {
                        continue;
                    }
                    request.getLabels().add(key);
                }
            }
        }
        return request;
    }

    public List<T> unWarp(BaseResponse<?> response) throws InstantiationException, IllegalAccessException {
        Object o = response.getData();
        if (!(o instanceof RdbmsDynamicFieldQueryResponse)) {
            log.error("BaseResponse not load RdbmsDynamicFieldQueryResponse");
            return null;
        }
        RdbmsDynamicFieldQueryResponse data = (RdbmsDynamicFieldQueryResponse) o;
        return unWarp(data.getContents());
    }

    public List<T> unWarp(List<HashMap<String, Object>> contents) throws IllegalAccessException, InstantiationException {
        if (contents == null) {
            log.error("RdbmsDynamicFieldQueryResponse contents is null");
            return null;
        }
        List<T> objects = new ArrayList<>(contents.size());
        for (HashMap<String, Object> content : contents) {
            T obj = clazz.newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                boolean bool = field.isAnnotationPresent(MsLabelField.class);
                if (bool) {
                    String label = field.getAnnotation(MsLabelField.class).value().toUpperCase();
                    if (content.containsKey(label)) {
                        Object value = content.get(label);
                        if (field.getType().equals(List.class)) {
                            value = StrUtil.split(value.toString(), ',');
                        } else {
                            if (value instanceof BigDecimal) {
                                switch (field.getType().toString()) {
                                    case "int":
                                    case "class java.lang.Integer":
                                        value = Integer.parseInt(value.toString());
                                        break;
                                    case "long":
                                    case "class java.lang.Long":
                                        value = Long.parseLong(value.toString());
                                        break;
                                    case "double":
                                    case "class java.lang.Double":
                                        value = Double.parseDouble(value.toString());
                                        break;
                                    case "float":
                                    case "class java.lang.Float":
                                        value = Float.parseFloat(value.toString());
                                        break;
                                    case "class java.lang.String":
                                        value = value.toString();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }
                        field.setAccessible(true);
                        field.set(obj, value);
                    }
                }
            }
            objects.add(obj);
        }
        return objects;
    }

    public MsQueryWrapper<T> eq(String key, String value) {
        request.getConditions().add(new MsQueryCondition(QueryKeyword.EQ, key, value));
        return this;
    }

    public MsQueryWrapper<T> ne(String key, String value) {
        request.getConditions().add(new MsQueryCondition(QueryKeyword.NE, key, value));
        return this;
    }

    public MsQueryWrapper<T> gt(String key, String value) {
        request.getConditions().add(new MsQueryCondition(QueryKeyword.GT, key, value));
        return this;
    }

    public MsQueryWrapper<T> ge(String key, String value) {
        request.getConditions().add(new MsQueryCondition(QueryKeyword.GE, key, value));
        return this;
    }

    public MsQueryWrapper<T> lt(String key, String value) {
        request.getConditions().add(new MsQueryCondition(QueryKeyword.LT, key, value));
        return this;
    }

    public MsQueryWrapper<T> le(String key, String value) {
        request.getConditions().add(new MsQueryCondition(QueryKeyword.LE, key, value));
        return this;
    }

    public MsQueryWrapper<T> like(String key, String value) {
        request.getConditions().add(new MsQueryCondition(QueryKeyword.LIKE, key, value));
        return this;
    }

    public MsQueryWrapper<T> isNull(String key) {
        request.getConditions().add(new MsQueryCondition(QueryKeyword.IS_NULL, key, null));
        return this;
    }

    public MsQueryWrapper<T> isNotNull(String key) {
        request.getConditions().add(new MsQueryCondition(QueryKeyword.IS_NOT_NULL, key, null));
        return this;
    }

}

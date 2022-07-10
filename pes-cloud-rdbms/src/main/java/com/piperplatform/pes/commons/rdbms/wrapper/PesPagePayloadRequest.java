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
package com.piperplatform.pes.commons.rdbms.wrapper;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.piperplatform.pes.commons.rdbms.annotation.TableAliasField;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * PesPagePayloadRequest
 *
 * @author shiyoufeng
 * @date 2021/3/31
 */
@Data
@Accessors(chain = true)
@Slf4j
public class PesPagePayloadRequest<T> {
    private Page<T> page;
    private T payload;

    public QueryWrapper<T> wrapper() throws IllegalAccessException {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        HashMap<String, String> fieldMap = new HashMap<>();
        ;

        Field[] fields = payload.getClass().getDeclaredFields();
        for (Field field : fields) {
            String tableFieldName = null;
            field.setAccessible(true);
            if (field.isAnnotationPresent(TableAliasField.class)) {
                tableFieldName = field.getAnnotation(TableAliasField.class).value().toUpperCase();
            } else if (field.isAnnotationPresent(TableField.class)) {
                tableFieldName = field.getAnnotation(TableField.class).value().toUpperCase();
            }

            if (tableFieldName != null) {
                // The querywrapper condition is built according to the query field name and field value
                Object fieldValue = field.get(payload);
                if (fieldValue != null) {
                    String val = String.valueOf(fieldValue).trim();
                    if (val.contains(":")) {
                        String[] split = val.split(":");
                        if (split.length == 2) {
                            if ("LIKE".equalsIgnoreCase(split[0])) {
                                wrapper.like(tableFieldName, split[1]);
                            } else if ("EQ".equalsIgnoreCase(split[0])) {
                                wrapper.eq(tableFieldName, split[1]);
                            }
                        }
                    } else {
                        wrapper.like(tableFieldName, String.valueOf(fieldValue).trim());
                    }
                }
                fieldMap.put(field.getName(), tableFieldName);
                if (tableFieldName.contains("MARK")) {
                    wrapper.eq(tableFieldName, 1);
                }
            }
        }
        if (page.getOrders() != null && page.getOrders().size() > 0) {
            // Update the sorting database field name according to the field database name
            for (OrderItem order : page.getOrders()) {
                if (fieldMap.containsKey(order.getColumn())) {
                    order.setColumn(fieldMap.get(order.getColumn()));
                }
            }
        } else {
            ArrayList<OrderItem> orders = new ArrayList<>();
            orders.add(new OrderItem("ID", true));
            page.setOrders(orders);
        }
        return wrapper;
    }

}

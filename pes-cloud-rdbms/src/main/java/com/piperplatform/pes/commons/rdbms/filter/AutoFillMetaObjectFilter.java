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
package com.piperplatform.pes.commons.rdbms.filter;

import cn.dev33.satoken.exception.SaTokenException;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Entity class field automatic injection interceptor
 *
 * @author shiyoufeng
 * @date 2021/3/31
 */
@Component
public class AutoFillMetaObjectFilter implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        long loginIdAsLong = getUserId();
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("createUser", loginIdAsLong, metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateUser", loginIdAsLong, metaObject);
        if (this.getFieldValByName("mark", metaObject) == null) {
            this.setFieldValByName("mark", 1, metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        long loginIdAsLong = getUserId();
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateUser", loginIdAsLong, metaObject);
    }

    private long getUserId() {
        long loginIdAsLong = 0;
        try {
            if (StpUtil.isLogin()) {
                loginIdAsLong = StpUtil.getLoginIdAsLong();
            }
        } catch (SaTokenException ignored) {
        }
        return loginIdAsLong == 0 ? 1L : loginIdAsLong;
    }
}

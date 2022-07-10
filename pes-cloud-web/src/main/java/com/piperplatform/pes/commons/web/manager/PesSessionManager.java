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
package com.piperplatform.pes.commons.web.manager;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.piperplatform.pes.commons.enums.PesConstants;
import com.piperplatform.pes.commons.util.ObjUtils;
import com.piperplatform.pes.commons.web.entity.dto.UserInfoDTO;
import com.piperplatform.pes.commons.web.entity.vo.TokenVO;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * PES 会话管理器
 * @author youfeng.shi
 * @date 2022/5/3
 */
public class PesSessionManager {
    private PesSessionManager() {
    }

    public static TokenVO login(Object id, boolean rm) {
        StpUtil.login(id, rm);
        SaTokenInfo tokenInfo = StpUtil.getTokenInfo();

        TokenVO tokenVO = new TokenVO();
        tokenVO.setTn(tokenInfo.tokenName)
                .setTv(tokenInfo.tokenValue)
                .setTt(tokenInfo.tokenTimeout);

        return tokenVO;
    }

    public static void logout() {
        StpUtil.logout();
    }
    public static UserInfoDTO userInfo() throws IllegalAccessException, InstantiationException {
        return get(PesConstants.USER_INFO, UserInfoDTO.class);
    }
    public static void set(String key, Object value) throws IllegalAccessException {
        if (!ObjUtils.isNativeType(value.getClass())) {
            Field[] fields = value.getClass().getDeclaredFields();
            HashMap<String, Object> data = new HashMap<>(16);
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldType = field.getType().toString();
                if (!ObjUtils.isNativeType(fieldType)) {
                    throw new IllegalAccessException(String.format("Cannot inject custom type field:[%s] key:[%s],value:[%s]", fieldType, key, value));
                }
                Object _value = field.get(value);
                if (_value != null) {
                    data.put(field.getName(), _value);
                }
            }
            value = data;
        }

        StpUtil.getSession().set(key, value);
    }

    public static void set(Object o) throws IllegalAccessException {
        set(o.getClass().getName(), o);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Class<T> o) throws InstantiationException, IllegalAccessException {
        return (T) get(o.getName(), o);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key, Class<T> clazz) throws IllegalAccessException, InstantiationException {
        if (ObjUtils.isNativeType(clazz)) {
            return (T) StpUtil.getSession().get(key);
        }
        HashMap<String, Object> data = (HashMap<String, Object>) StpUtil.getSession().get(key);
        T result = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isFinal(field.getModifiers())) {
                continue;
            }
            String filename = field.getName();
            if (data.containsKey(filename)) {
                field.setAccessible(true);
                field.set(result, data.get(filename));
            }
        }
        return result;
    }
}

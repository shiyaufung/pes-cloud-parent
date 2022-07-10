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
package com.piperplatform.pes.commons.web.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * RedisUtils
 *
 * @author youfeng.shi
 * @date 2022/5/4
 */
public class RedisUtils {

    private static RedisTemplate redisTemplate;

    private RedisUtils() {
    }

    @SuppressWarnings("unchecked")
    public static boolean set(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        if (redisTemplate == null) {
            redisTemplate = SpringUtils.getBean("redisTemplate");
        }
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        return redisTemplate.expire(key, expireTime, timeUnit);
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(String key) {
        if (redisTemplate == null) {
            redisTemplate = SpringUtils.getBean("redisTemplate");
        }
        return (T) redisTemplate.opsForValue().get(key);
    }

    @SuppressWarnings("unchecked")
    public static boolean delete(String key) {
        if (redisTemplate == null) {
            redisTemplate = SpringUtils.getBean("redisTemplate");
        }

        return redisTemplate.delete(key);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAndDelete(String key) {
        if (redisTemplate == null) {
            redisTemplate = SpringUtils.getBean("redisTemplate");
        }
        T result = (T)redisTemplate.opsForValue().get(key);
        redisTemplate.delete(key);
        return result;
    }

}

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
package com.piperplatform.pes.commons.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;

/**
 * @author shiyoufeng
 * @date 2021/9/6
 */
public class StrUtils extends StrUtil {
    private StrUtils(){}

    public static final String NUMBER_REGEX = "([0-9]\\d*\\.?\\d*)||(0\\.\\d*[1-9])";

    /**
     * Whether to include string
     *
     * @param str  Validation string
     * @param strs String array
     * @return Returns true if it contains
     */
    public static boolean inStringIgnoreCase(String str, String... strs) {
        if (str != null && strs != null) {
            for (String s : strs) {
                if (str.equalsIgnoreCase(trim(s))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Get Tik style UUID 36 bits
     *
     * @return 36 bit Tik UUID
     */
    public static String getTikUid() {
        String[] uidArr = StrUtils.split(IdUtil.fastSimpleUUID(), 1);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < uidArr.length; i++) {
            if (RandomUtil.randomInt() % 2 == 0) {
                uidArr[i] = uidArr[i].toUpperCase();
            }
            sb.append(uidArr[i]);
            switch (i) {
                case 1:
                case 16:
                    sb.append("_");
                    break;
                case 31:
                    sb.append("_");
                    sb.append("_");
                default:
                    break;
            }
        }
        return sb.toString();
    }

    /**
     * Convert string to uppercase
     * @param s Target string
     * @return Convert uppercase string
     */
    public static String toUpperCase(String s) {
        return s.toUpperCase();
    }

    /**
     * Determine whether the string is a number
     * @param s Target string
     * @return true It's a number
     */
    public static boolean isNumber(String s) {
        return s.matches(NUMBER_REGEX);
    }
}

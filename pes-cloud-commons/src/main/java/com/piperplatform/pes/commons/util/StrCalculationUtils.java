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

import cn.hutool.core.util.StrUtil;

import java.math.BigDecimal;

/**
 * @author youfeng.shi
 * @date 2022/2/18
 */
public class StrCalculationUtils {
    private StrCalculationUtils() {
    }

    public static String add(String s1, String s2) {
        if (!StrUtil.isNotEmpty(s1)) {
            s1 = s2;
        } else {
            s1 = new BigDecimal(s1)
                    .add(new BigDecimal(s2))
                    .toPlainString();
        }
        return s1;
    }

    public static String sub(String s1, String s2) {
        return new BigDecimal(s1)
                .subtract(new BigDecimal(s2))
                .toPlainString();
    }

    public static String mul(String s1, String s2) {
        return new BigDecimal(s1)
                .multiply(new BigDecimal(s2))
                .toPlainString();
    }

    public static String mulFloor(String s1, String s2) {
        return new BigDecimal(s1)
                .multiply(new BigDecimal(s2))
                .setScale(2, BigDecimal.ROUND_FLOOR)
                .toPlainString();
    }

    public static String div(String s1, String s2, int scale, int roundingMode) {
        return new BigDecimal(s1)
                .divide(new BigDecimal(s2), scale, roundingMode)
                .toPlainString();
    }

    public static String div(String s1, String s2) {
        if (StrUtils.isEmpty(s2)) {
            return "0";
        }
        double d = Double.parseDouble(s2);
        if (d == 0) {
            return "0";
        }
        return div(s1, s2, 2, BigDecimal.ROUND_HALF_UP);
    }

    public static String divFloor(String s1, String s2) {
        if (StrUtils.isEmpty(s2)) {
            return "0";
        }
        double d = Double.parseDouble(s2);
        if (d == 0) {
            return "0";
        }
        return div(s1, s2, 2, BigDecimal.ROUND_FLOOR);
    }
}

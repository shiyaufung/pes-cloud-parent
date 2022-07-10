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

/**
 * @author youfeng.shi
 * @date 2022/5/9
 */
public class ObjUtils {
    public static final String CLASS = "class";
    public static final String INTERFACE = "interface";

    private ObjUtils() {
    }

    public static boolean isNativeType(Class clazz) {
        return isNativeType(clazz.getTypeName());
    }

    public static boolean isNativeType(String typeName) {
        if (typeName.startsWith(CLASS)) {
            typeName = typeName.substring(CLASS.length() + 1);
        }else if (typeName.startsWith(INTERFACE)){
            typeName = typeName.substring(INTERFACE.length() + 1);
        }
        if (typeName.contains(".") && !typeName.startsWith("java")) {
            return false;
        }
        return true;
    }
}

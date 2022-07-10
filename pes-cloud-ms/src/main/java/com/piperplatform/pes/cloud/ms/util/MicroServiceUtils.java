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
package com.piperplatform.pes.cloud.ms.util;

import com.piperplatform.pes.commons.entity.MsSession;

/**
 * Microservice tools
 *
 * @author youfeng.shi
 * @date 2022/4/19
 */
public class MicroServiceUtils {

    private static final ThreadLocal<MsSession> LOCAL_VAR = new ThreadLocal<>();

    private MicroServiceUtils() {
    }

    public static void setSession(MsSession session) {
        LOCAL_VAR.set(session);
    }

    public static MsSession getSession() {
        return LOCAL_VAR.get();
    }

    public static void clearSession() {
        LOCAL_VAR.remove();
    }
}

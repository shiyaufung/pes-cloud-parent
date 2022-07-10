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
package com.piperplatform.pes.commons.manager;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;

/**
 * @author youfeng.shi
 * @date 2022/4/28
 */
public class PesEnvironmentPostProcessor {

    private byte[] k = null;
    private static final int KEY_SIZE = 32;

    private SymmetricCrypto s(byte[] k) {
        return new SymmetricCrypto(SymmetricAlgorithm.AES, k);
    }

    protected String e(String s) {
        String r = "";
        try {
            r = s(getK()).decryptStr(s, CharsetUtil.CHARSET_UTF_8);
        } catch (Exception ignore) {
        }

        return r;
    }

    protected String d(String s) {
        return s(getK()).encryptHex(s);
    }

    private byte[] getK() {
        if (k == null) {
            String cn = this.getClass().getSimpleName();
            StringBuilder sb = new StringBuilder();
            if (cn.length() < KEY_SIZE) {
                sb.append(cn);
                for (int i = 0; i < KEY_SIZE - cn.length(); i++) {
                    sb.append("*");
                }
                cn = sb.toString();
            }
            k = cn.getBytes();
        }
        return k;
    }
}

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
package com.piperplatform.pes.cloud.ms.filter;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.piperplatform.pes.cloud.ms.util.MicroServiceUtils;
import com.piperplatform.pes.commons.entity.MsSession;
import com.piperplatform.pes.commons.enums.PesConstants;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * Microservice distributed authentication filter
 * @author youfeng.shi
 * @date 2022/4/22
 */
@Activate(group = {CommonConstants.CONSUMER, CommonConstants.PROVIDER}, order = -10000)
public class PreAuthFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        boolean consumerSide = RpcContext.getContext().isConsumerSide();

        if (consumerSide) {
            try {
                RpcContext.getContext().setAttachment(PesConstants.ID_TOKEN, StpUtil.getTokenValue());
                RpcContext.getContext().setAttachment(PesConstants.SC, SpringUtil.getProperty("spring.application.security-code"));
                RpcContext.getContext().setAttachment(PesConstants.HOST, SpringUtil.getProperty("server.address"));
                RpcContext.getContext().setAttachment(PesConstants.PORT, SpringUtil.getProperty("server.port"));
                RpcContext.getContext().setAttachment(PesConstants.INTERFACE_NAME, RpcContext.getContext().getInterfaceName());
            } catch (Exception ignore) {
            }
        } else {
            MsSession msSession = null;
            String token = invocation.getAttachment(PesConstants.ID_TOKEN);

            if (StrUtil.isNotEmpty(token)) {
                msSession = (MsSession) StpUtil.getTokenSessionByToken(token).get(PesConstants.SESSION);
            }
            if (msSession == null) {
                msSession = new MsSession();
            }

            msSession.setAppName(RpcContext.getContext().getRemoteApplicationName())
                    .setHost(invocation.getAttachment(PesConstants.HOST))
                    .setPort(invocation.getAttachment(PesConstants.PORT))
                    .setSc(invocation.getAttachment(PesConstants.SC))
                    .setInterfaceName(invocation.getAttachment(PesConstants.INTERFACE_NAME));

            MicroServiceUtils.setSession(msSession);
        }

        try {
            return invoker.invoke(invocation);
        } finally {
            MicroServiceUtils.clearSession();
        }
    }
}

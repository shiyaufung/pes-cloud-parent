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
package com.piperplatform.pes.commons.web.config;

import cn.dev33.satoken.interceptor.SaRouteInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.piperplatform.pes.commons.enums.PesConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * PES Framework default interceptor rule
 *
 * @author youfeng.shi
 * @date 2022/4/19
 */
@Configuration
@Slf4j
public class WebInterceptorConfig implements WebMvcConfigurer {

    @Value("${spring.application.api-url}")
    private String apiUrl;

    @Value("${spring.profiles.active}")
    private String env;

    @Autowired(required = false)
    private ExcludeWebInterceptors excludeWebInterceptors;

    private List<String> excludePatterns;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if (excludePatterns == null) {
            excludePatterns = new ArrayList<>();
            excludePatterns.add("/");
            excludePatterns.add("/favicon.ico");
            excludePatterns.add(apiUrl + "/auth/captcha");
            excludePatterns.add(apiUrl + "/auth/login");
            if (!StrUtil.equals(env, PesConstants.ENV_PROD)) {
                excludePatterns.addAll(Arrays.asList(
                        apiUrl + "/auth/dev-login",
                        apiUrl + "/auth/md5",
                        "/swagger-ui.html",
                        "/swagger/**",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/v3/**",
                        "/doc.html"));
            }
            if (excludeWebInterceptors != null) {
                excludePatterns.addAll(excludeWebInterceptors.get());
            }
        }

        registry.addInterceptor(new SaRouteInterceptor((req, res, handler) ->
                        SaRouter.match("/**", StpUtil::checkLogin)))
                .addPathPatterns("/**")
                .excludePathPatterns(excludePatterns);

    }
}

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

import com.piperplatform.pes.commons.manager.PesEnvironmentPostProcessor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.*;

/**
 * PesWebEnvironmentPostProcessor
 *
 * The microservice application accepts the unified configuration of the configuration center
 *
 * @author youfeng.shi
 * @date 2022/4/18
 */
public class PesWebEnvironmentPostProcessor extends PesEnvironmentPostProcessor implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        Map<String, Object> springMap = getSourceMap(environment, "spring");
        Map<String, Object> dubboMap = getSourceMap(environment, "dubbo");
        Map<String, Object> managementMap = getSourceMap(environment, "management");

        springMap.put("spring.main.allow-circular-references", true);
        springMap.put("spring.cloud.nacos.server-addr", e("e7ec802d9a093125eb4a66ee5f86a4f8c076f013000da3f3e930312aad885698"));
        springMap.put("spring.cloud.nacos.config.server-addr", e("e7ec802d9a093125eb4a66ee5f86a4f8c076f013000da3f3e930312aad885698"));
        springMap.put("spring.cloud.nacos.config.prefix", "commons");
        springMap.put("spring.cloud.nacos.config.file-extension", "yaml");
        springMap.put("spring.cloud.nacos.discovery.username", e("8372d729ff3aa42bc07e68185f9e5e25e7c404e59106bd513fcaeb4004fae52a"));
        springMap.put("spring.cloud.nacos.discovery.password", e("9097a309d17579a2b5a027f2185105696877d22b12ef47371e8c44bad536b293"));
        springMap.put("spring.cloud.nacos.discovery.ip", "${server.address}");

        dubboMap.put("dubbo.scan.base-packages", "${spring.application.base-package}");
        dubboMap.put("dubbo.protocol.name", "dubbo");
        dubboMap.put("dubbo.protocol.host", "${server.address}");
        dubboMap.put("dubbo.protocol.port", -1);
        dubboMap.put("dubbo.registry.address", "spring-cloud://localhost");
        dubboMap.put("dubbo.consumer.timeout", 30000);
        dubboMap.put("dubbo.provider.timeout", 30000);

        managementMap.put("management.endpoints.web.exposure.include", "*");

        environment.getPropertySources().addLast(new MapPropertySource("spring", springMap));
        environment.getPropertySources().addLast(new MapPropertySource("dubbo", dubboMap));
        environment.getPropertySources().addLast(new MapPropertySource("management", managementMap));
    }

    private Map<String, Object> getSourceMap(ConfigurableEnvironment environment, String name) {
        Map<String, Object> result = null;
        if (environment.getPropertySources().contains(name)) {
            MapPropertySource map = (MapPropertySource) environment.getPropertySources().get(name);
            if (map != null) {
                result = map.getSource();
            }
        } else {
            result = new HashMap<>();
            environment.getPropertySources().addLast(new MapPropertySource(name, result));
        }
        return result;
    }

}

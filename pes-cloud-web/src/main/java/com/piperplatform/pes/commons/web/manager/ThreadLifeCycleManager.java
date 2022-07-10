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

import cn.hutool.core.util.StrUtil;
import com.piperplatform.pes.commons.manager.ThreadManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

/**
 * pes.common.web The thread manager is used to manage the thread lifecycle
 *
 * @author shiyoufeng
 * @date 2022/1/11
 */
@Component
@Slf4j
public class ThreadLifeCycleManager implements CommandLineRunner {

    @Value("${spring.system.thread.core.pool-size:128}")
    private int coreThreadPoolSize;

    @Value("${spring.system.thread.task.pool-size:1024}")
    private int taskThreadPoolSize;

    @Value("${spring.system.thread.task.max-pool-size:32767}")
    private int taskThreadPoolMaxSize;

    @Value("${spring.system.thread.task.keep-alive-time:300}")
    private int taskThreadPoolKeepAliveTime;

    @Value("${spring.application.name}")
    private String applicationName;

    @PreDestroy
    public void destroy() {
        try {
            // Close thread pool
            log.debug("====Thread Lifecycle Manager: trying to close the background task thread pool====");
            ThreadManager.shutdown();
            log.debug("====Thread Lifecycle Manager: close background task thread pool completion====");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void run(String... args) {
        log.debug("====Thread Lifecycle Manager: trying to initialize the core thread pool====");
        ThreadManager.buildCoreThreadPool(StrUtil.format("pes.{}.core", applicationName), coreThreadPoolSize);
        ThreadManager.buildTaskThreadPool(StrUtil.format("pes.{}.task", applicationName), taskThreadPoolSize, taskThreadPoolMaxSize, TimeUnit.SECONDS.toNanos(taskThreadPoolKeepAliveTime));
        log.debug("====Thread Lifecycle Manager: initialize core thread pool complete====");
    }
}

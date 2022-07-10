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

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.core.thread.ThreadUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * Thread manager
 *
 * @author shiyoufeng
 * @date 2021/9/9
 */
@Slf4j
public class ThreadManager extends ThreadUtil {

    /**
     * Core thread pool
     */
    private static ThreadPoolExecutor CORE_THREAD_POOL;

    /**
     * Task thread pool
     */
    private static ThreadPoolExecutor TASK_THREAD_POOL;

    /**
     * Build core thread pool
     *
     * @param prefix   Thread pool name prefix
     * @param poolSize Thread pool size
     */
    public static void buildCoreThreadPool(String prefix, int poolSize) {
        if (CORE_THREAD_POOL != null) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Build core thread pool prefix:{} , poolSize:{}", prefix, poolSize);
        }
        CORE_THREAD_POOL = ExecutorBuilder
                .create()
                .setCorePoolSize(poolSize)
                .setMaxPoolSize(poolSize)
                .setKeepAliveTime(0L)
                .setThreadFactory(createPoolFactory(prefix))
                .build();
    }

    /**
     * Build task thread pool
     *
     * @param prefix        Thread pool name prefix
     * @param poolSize      Thread pool size
     * @param maxPoolSize   Maximum thread pool size
     * @param keepAliveTime Thread keepalive time
     */
    public static void buildTaskThreadPool(String prefix, int poolSize, int maxPoolSize, long keepAliveTime) {
        if (TASK_THREAD_POOL != null) {
            return;
        }
        if (log.isDebugEnabled()) {
            log.debug("Build task thread pool prefix:{} , poolSize:{} , maxPoolSize:{} , keepAliveTime:{}", prefix, poolSize, maxPoolSize, keepAliveTime);
        }
        TASK_THREAD_POOL = ExecutorBuilder
                .create()
                .setCorePoolSize(poolSize)
                .setMaxPoolSize(maxPoolSize)
                .setKeepAliveTime(keepAliveTime)
                .setThreadFactory(createPoolFactory(prefix))
                .setWorkQueue(new LinkedBlockingQueue<>(maxPoolSize))
                .build();
    }

    /**
     * Close thread pool
     */
    public static void shutdown() {
        shutdownAndAwaitTermination(CORE_THREAD_POOL);
        shutdownAndAwaitTermination(TASK_THREAD_POOL);
    }

    /**
     * Core thread task submission (has a return value)
     *
     * @param task Thread with return value
     * @param <T>  Return type generic
     * @return Return thread call result
     */
    public static <T> Future<T> coreSubmit(Callable<T> task) {
        return CORE_THREAD_POOL.submit(task);
    }

    /**
     * Core thread task submission (no return value)
     *
     * @param runnable thread
     */
    public static void coreExecute(Runnable runnable) {
        CORE_THREAD_POOL.execute(runnable);
    }

    /**
     * Task thread task submission (has return value)
     *
     * @param task Thread with return value
     * @param <T>  Return type generic
     * @return Return thread call result
     */
    public static <T> Future<T> taskSubmit(Callable<T> task) {
        return TASK_THREAD_POOL.submit(task);
    }

    /**
     * Task thread task submission (no return value)
     *
     * @param runnable thread
     */
    public static void taskExecute(Runnable runnable) {
        TASK_THREAD_POOL.execute(runnable);
    }

    /**
     * Thread factory
     *
     * @param prefix Thread pool name prefix
     * @return Thread factory
     */
    private static ThreadFactory createPoolFactory(String prefix) {
        return ThreadUtil.newNamedThreadFactory(prefix, null, false, (t, e) -> {
            log.error(ExceptionUtil.stacktraceToString(e));
        });
    }

    /**
     * Stop thread pool
     * First use shutdown to stop receiving new tasks and try to complete all existing tasks
     * If it times out, call shutdownnow, cancel the pending task in the workqueue, and interrupt all blocking functions
     * If it still times out, it will be forced to exit
     * In addition, the thread itself is called to interrupt during shutdown
     */
    public static void shutdownAndAwaitTermination(ExecutorService pool) {
        if (pool != null && !pool.isShutdown()) {
            pool.shutdown();
            try {
                if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                    pool.shutdownNow();
                    if (!pool.awaitTermination(5, TimeUnit.SECONDS)) {
                        throw new InterruptedException("Pool did not terminate");
                    }
                }
                log.debug("Pool shutdown successfully.");
            } catch (InterruptedException ie) {
                log.info("Pool did not terminate try force shutdown.");
                pool.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * Print thread exception information
     */
    public static void printException(Runnable r, Throwable t) {
        if (t == null && r instanceof Future<?>) {
            try {
                Future<?> future = (Future<?>) r;
                if (future.isDone()) {
                    future.get();
                }
            } catch (CancellationException ce) {
                t = ce;
            } catch (ExecutionException ee) {
                t = ee.getCause();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
        if (t != null) {
            log.error(t.getMessage(), t);
        }
    }

    /**
     * private
     */
    private ThreadManager() {
    }
}

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
package com.piperplatform.pes.commons.web.filter;

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.OutputStreamAppender;
import ch.qos.logback.core.spi.DeferredProcessingAware;
import com.piperplatform.pes.commons.web.entity.dto.LogDTO;

import java.io.OutputStream;

/**
 * Custom log interception appender
 *
 * @author shiyoufeng
 * @since 2021-08-17
 */
public class CustomLogInterceptAppender<E> extends OutputStreamAppender<E> {

    /**
     * External adder interface
     */
    private static LogInterceptEven even;

    /**
     * Register external appender
     *
     * @param even
     */
    public static void withEven(LogInterceptEven even) {
        CustomLogInterceptAppender.even = even;
    }

    @Override
    protected void subAppend(E event) {
        if (this.isStarted()) {
            if (event instanceof DeferredProcessingAware) {
                ((DeferredProcessingAware) event).prepareForDeferredProcessing();
            }
            if (even != null && event instanceof LoggingEvent) {
                LoggingEvent loggingEvent = (LoggingEvent) event;
                // Log transfer object
                LogDTO logDto = new LogDTO();
                // analysis
                logDto.setTimeStamp(loggingEvent.getTimeStamp())
                        .setLevel(loggingEvent.getLevel().levelStr)
                        .setThreadName(loggingEvent.getThreadName())
                        .setMessage(loggingEvent.getMessage());
                // information
                StackTraceElement[] callerDataArr = loggingEvent.getCallerData();
                if (callerDataArr != null && callerDataArr.length > 0) {
                    logDto.setClassName(callerDataArr[0].getClassName())
                            .setFileName(callerDataArr[0].getFileName())
                            .setMethodName(callerDataArr[0].getMethodName())
                            .setLineNumber(callerDataArr[0].getLineNumber());
                }
                even.even(logDto);
            }
        }
    }


    @Override
    public void start() {
        this.setOutputStream(new OutputStream() {
            @Override
            public void write(int b) {
            }
        });
        super.start();
    }

    public CustomLogInterceptAppender() {
    }

    public interface LogInterceptEven {
        /**
         * Trigger action after log interception
         *
         * @param logDTO Log transfer object
         */
        void even(LogDTO logDTO);
    }

}

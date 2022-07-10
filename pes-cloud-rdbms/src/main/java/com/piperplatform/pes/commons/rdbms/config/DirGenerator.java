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
package com.piperplatform.pes.commons.rdbms.config;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.io.FileUtil;
import com.piperplatform.pes.commons.entity.BaseEntry;
import com.piperplatform.pes.commons.util.StrUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Data label dictionary code generator
 * @author youfeng.shi
 * @date 2022/4/26
 */
@Component
public class DirGenerator {

    public void build(String className, String comment, List<BaseEntry> entries) throws IOException {
        String packageName = "generator";
        String author = "PesCodeGenerator";
        String date = LocalDateTimeUtil.format(LocalDate.now(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String projectPath = System.getProperty("user.dir");
        String filePath = String.format("%s%s%s%s", projectPath, "/src/main/java/generator/", className, ".java");

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
        velocityEngine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
        velocityEngine.init();

        VelocityContext context = new VelocityContext();
        context.put("StringUtils", StrUtils.class);
        context.put("package", packageName);
        context.put("author", author);
        context.put("date", date);
        context.put("className", className);
        context.put("comment", comment);
        context.put("entries", entries);

        Template template = velocityEngine.getTemplate("templates/dir.java.vm");

        try (OutputStreamWriter out = new OutputStreamWriter(FileUtil.getOutputStream(filePath), StandardCharsets.UTF_8);
             BufferedWriter writer = new BufferedWriter(out)
        ) {
            template.merge(context, writer);
            writer.flush();
        }
    }

}

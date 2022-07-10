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

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.piperplatform.pes.commons.entity.BaseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Database mapping code generator
 * @author youfeng.shi
 * @since 2021-05-28
 */
@Component
public class CodeGenerator {

    @Value("${spring.application.base-package}")
    private String packageParentName;

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.driver-class-name}")
    private String jdbcDriver;

    @Value("${spring.datasource.username}")
    private String jdbcUsername;

    @Value("${spring.datasource.password}")
    private String jdbcPassword;

    private Class superEntityClass;
    private String[] superEntityColumns;

    public void build(String[] includeTables) {
        build(includeTables, false, false);
    }

    public void build(String[] includeTables, boolean custom, boolean removePrefix) {
        final String moduleName = "generator";
        final String author = "PesCodeGenerator";
        if (!custom) {
            superEntityClass = BaseEntity.class;
            superEntityColumns = new String[]{"id", "create_user", "create_time", "update_user", "update_time", "mark"};
        }
        // 1、Create code generator
        AutoGenerator mpg = new AutoGenerator();
        // 2、configuration
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setAuthor(author);
        // Whether to open Explorer after generation
        gc.setOpen(false);
        // Whether the file is overwritten during regeneration
        gc.setFileOverride(true);
        // Remove the initial I of the service interface
        gc.setServiceName("%sService");
        // Turn on swagger2 mode
        gc.setSwagger2(true);

        mpg.setGlobalConfig(gc);

        // 3、Data source configuration
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl(jdbcUrl);
        dsc.setDriverName(jdbcDriver);
        dsc.setUsername(jdbcUsername);
        dsc.setPassword(jdbcPassword);
        mpg.setDataSource(dsc);

        // 4、Package configuration
        PackageConfig pc = new PackageConfig();
        pc.setParent(moduleName);
        pc.setController("controller");
        pc.setEntity("entity");
        pc.setService("service");
        pc.setMapper("mapper");
        mpg.setPackageInfo(pc);

        // 5、Policy configuration
        StrategyConfig strategy = new StrategyConfig();
        // Generate code for which table
        strategy.setInclude(includeTables);
        // Naming strategy for mapping database tables to entities
        strategy.setNaming(NamingStrategy.underline_to_camel);
        // Remove the table prefix when generating entities
        if (removePrefix) {
            String[] tablePrefix = new String[includeTables.length];
            for (int i = 0; i < includeTables.length; i++) {
                tablePrefix[i] = includeTables[i].substring(0, includeTables[i].indexOf("_") + 1);
            }
            strategy.setTablePrefix(tablePrefix);
        }
        // Naming strategy for mapping database table fields to entities
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // lombok Model
        strategy.setEntityLombokModel(true);
        // @Accessors(chain = true) setter chain operation
        strategy.setChainModel(true);
        // Exclude fields
        if (superEntityColumns != null && superEntityColumns.length > 0) {
            strategy.setSuperEntityColumns(superEntityColumns);
        }
        // Parent class base class
        if (superEntityClass != null) {
            strategy.setSuperEntityClass(superEntityClass);
        }
        // restful API style controller
        strategy.setRestControllerStyle(true);
        // hyphen in URL
        strategy.setControllerMappingHyphenStyle(true);
        mpg.setStrategy(strategy);

        // Set a custom template for adapting spring doc annotations
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig
                .setEntity("/templates/entity.java")
                .setEntityKt("/templates/entity.kt");
        mpg.setTemplate(templateConfig);

        // 6、execute
        mpg.execute();
    }

}

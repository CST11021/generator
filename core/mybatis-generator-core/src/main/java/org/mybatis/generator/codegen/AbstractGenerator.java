/*
 *    Copyright 2006-2023 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Context;

import java.util.List;

public abstract class AbstractGenerator {
    /** 上下文配置 */
    protected Context context;
    /** table的相信配置信息 */
    protected IntrospectedTable introspectedTable;
    /** 用于保存告警信息 */
    protected List<String> warnings;
    /** 用于扩展的回调程序 */
    protected ProgressCallback progressCallback;

    protected AbstractGenerator() {
        super();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setIntrospectedTable(IntrospectedTable introspectedTable) {
        this.introspectedTable = introspectedTable;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public void setProgressCallback(ProgressCallback progressCallback) {
        this.progressCallback = progressCallback;
    }
}

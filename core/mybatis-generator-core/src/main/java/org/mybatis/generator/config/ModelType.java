/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.config;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * defaultModelType：控制Domain类的生成行为。执行引擎为MyBatis3DynamicSql或者MyBatis3Kotlin时忽略此配置，可选值：
 * 
 * @author Jeff Butler
 */
public enum ModelType {

    /** 默认值，类似hierarchical，但是只有一个主键的时候会合并所有属性生成在同一个类 */
    HIERARCHICAL("hierarchical"),
    /** 所有内容全部生成在一个对象中 */
    FLAT("flat"),
    /** 键生成一个XXKey对象，Blob等单独生成一个对象，其他简单属性在一个对象中 */
    CONDITIONAL("conditional");

    private final String modelType;

    /**
     * 
     */
    private ModelType(String modelType) {
        this.modelType = modelType;
    }

    public String getModelType() {
        return modelType;
    }

    public static ModelType getModelType(String type) {
        if (HIERARCHICAL.getModelType().equalsIgnoreCase(type)) {
            return HIERARCHICAL;
        } else if (FLAT.getModelType().equalsIgnoreCase(type)) {
            return FLAT;
        } else if (CONDITIONAL.getModelType().equalsIgnoreCase(type)) {
            return CONDITIONAL;
        } else {
            throw new RuntimeException(getString("RuntimeError.13", type));
        }
    }
}

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
package org.mybatis.generator.runtime.dynamic.sql.elements;

import java.util.HashSet;
import java.util.Set;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;

public class UpdateAllColumnsMethodGenerator extends AbstractMethodGenerator {
    private final FullyQualifiedJavaType recordType;
    private final FragmentGenerator fragmentGenerator;

    private UpdateAllColumnsMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
        fragmentGenerator = builder.fragmentGenerator;
    }

    @Override
    public MethodAndImports generateMethodAndImports() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        FullyQualifiedJavaType parameterAndReturnType = new FullyQualifiedJavaType(
                "org.mybatis.dynamic.sql.update.UpdateDSL");
        parameterAndReturnType.addTypeArgument(new FullyQualifiedJavaType(
                "org.mybatis.dynamic.sql.update.UpdateModel"));
        imports.add(parameterAndReturnType);

        imports.add(recordType);

        Method method = new Method("updateAllColumns");
        method.setStatic(true);
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);

        method.setReturnType(parameterAndReturnType);
        method.addParameter(new Parameter(recordType, "row"));
        method.addParameter(new Parameter(parameterAndReturnType, "dsl"));

        method.addBodyLines(fragmentGenerator.getSetEqualLines(introspectedTable.getAllColumns(),
                "return dsl", "        ", true));  

        return MethodAndImports.withMethod(method)
                .withImports(imports)
                .build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return context.getPlugins().clientUpdateAllColumnsMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {
        private FullyQualifiedJavaType recordType;
        private FragmentGenerator fragmentGenerator;

        public Builder withRecordType(FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }

        public Builder withFragmentGenerator(FragmentGenerator fragmentGenerator) {
            this.fragmentGenerator = fragmentGenerator;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public UpdateAllColumnsMethodGenerator build() {
            return new UpdateAllColumnsMethodGenerator(this);
        }
    }
}

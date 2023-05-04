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

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.config.GeneratedKey;

import java.util.HashSet;
import java.util.Set;

public class BasicMultipleInsertMethodGenerator extends AbstractMethodGenerator {

    private final FullyQualifiedJavaType recordType;

    private BasicMultipleInsertMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
    }

    @Override
    public MethodAndImports generateMethodAndImports() {
        if (!Utils.generateMultipleRowInsert(introspectedTable)) {
            return null;
        }

        return introspectedTable.getGeneratedKey().map(this::generateMethodWithGeneratedKeys).orElse(null);
    }

    private MethodAndImports generateMethodWithGeneratedKeys(GeneratedKey gk) {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.SqlProviderAdapter"));
        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.InsertProvider"));
        imports.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Param"));

        Parameter parm1 = new Parameter(FullyQualifiedJavaType.getStringInstance(), "insertStatement");
        parm1.addAnnotation("@Param(\"insertStatement\")");

        FullyQualifiedJavaType recordListType = FullyQualifiedJavaType.getNewListInstance();
        recordListType.addTypeArgument(recordType);
        imports.add(recordListType);

        Parameter parm2 = new Parameter(recordListType, "records");
        parm2.addAnnotation("@Param(\"records\")");

        Method method = new Method("insertMultiple");
        method.setAbstract(true);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(parm1);
        method.addParameter(parm2);
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.addAnnotation("@InsertProvider(type=SqlProviderAdapter.class, "
                + "method=\"insertMultipleWithGeneratedKeys\")");

        MethodAndImports.Builder builder = MethodAndImports.withMethod(method)
                .withImports(imports);

        MethodParts methodParts = getGeneratedKeyAnnotation(gk);
        acceptParts(builder, method, methodParts);

        return builder.build();
    }

    private MethodParts getGeneratedKeyAnnotation(GeneratedKey gk) {
        MethodParts.Builder builder = new MethodParts.Builder();

        StringBuilder sb = new StringBuilder();
        introspectedTable.getColumn(gk.getColumn()).ifPresent(introspectedColumn -> {
            if (gk.isJdbcStandard()) {
                // only jdbc standard keys are supported for multiple insert
                builder.withImport(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Options"));
                sb.append("@Options(useGeneratedKeys=true,keyProperty=\"records.");
                sb.append(introspectedColumn.getJavaProperty());
                sb.append("\")");
                builder.withAnnotation(sb.toString());
            }
        });

        return builder.build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return context.getPlugins().clientBasicInsertMultipleMethodGenerated(method, interfaze, introspectedTable);
    }

    public static class Builder extends BaseBuilder<Builder> {

        private FullyQualifiedJavaType recordType;

        public Builder withRecordType(FullyQualifiedJavaType recordType) {
            this.recordType = recordType;
            return this;
        }

        @Override
        public Builder getThis() {
            return this;
        }

        public BasicMultipleInsertMethodGenerator build() {
            return new BasicMultipleInsertMethodGenerator(this);
        }
    }
}

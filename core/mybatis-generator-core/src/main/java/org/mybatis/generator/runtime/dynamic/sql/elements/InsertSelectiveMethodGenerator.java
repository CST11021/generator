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
import java.util.List;
import java.util.Set;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.Parameter;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.internal.util.JavaBeansUtil;

public class InsertSelectiveMethodGenerator extends AbstractMethodGenerator {
    private final FullyQualifiedJavaType recordType;

    private InsertSelectiveMethodGenerator(Builder builder) {
        super(builder);
        recordType = builder.recordType;
    }

    @Override
    public MethodAndImports generateMethodAndImports() {
        Set<FullyQualifiedJavaType> imports = new HashSet<>();

        imports.add(new FullyQualifiedJavaType("org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils"));
        imports.add(recordType);

        Method method = new Method("insertSelective");
        method.setDefault(true);
        context.getCommentGenerator().addGeneralMethodAnnotation(method, introspectedTable, imports);
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.addParameter(new Parameter(recordType, "row"));

        method.addBodyLine("return MyBatis3Utils.insert(this::insert, row, " + tableFieldName
                + ", c ->");

        List<IntrospectedColumn> columns = ListUtilities.removeIdentityAndGeneratedAlwaysColumns(
                introspectedTable.getAllColumns());
        boolean first = true;
        for (IntrospectedColumn column : columns) {
            String fieldName = calculateFieldName(column);
            if (column.isSequenceColumn()) {
                if (first) {
                    method.addBodyLine("    c.map(" + fieldName
                            + ").toProperty(\"" + column.getJavaProperty()
                            + "\")");
                    first = false;
                } else {
                    method.addBodyLine("    .map(" + fieldName
                            + ").toProperty(\"" + column.getJavaProperty()
                            + "\")");
                }
            } else {
                String methodName =
                        JavaBeansUtil.getGetterMethodName(column.getJavaProperty(),
                                column.getFullyQualifiedJavaType());
                if (first) {
                    method.addBodyLine("    c.map(" + fieldName
                            + ").toPropertyWhenPresent(\"" + column.getJavaProperty()
                            + "\", row::" + methodName
                            + ")");
                    first = false;
                } else {
                    method.addBodyLine("    .map(" + fieldName
                            + ").toPropertyWhenPresent(\"" + column.getJavaProperty()
                            + "\", row::" + methodName
                            + ")");
                }
            }
        }

        method.addBodyLine(");");

        return MethodAndImports.withMethod(method)
                .withImports(imports)
                .build();
    }

    @Override
    public boolean callPlugins(Method method, Interface interfaze) {
        return context.getPlugins().clientInsertSelectiveMethodGenerated(method, interfaze, introspectedTable);
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

        public InsertSelectiveMethodGenerator build() {
            return new InsertSelectiveMethodGenerator(this);
        }
    }
}

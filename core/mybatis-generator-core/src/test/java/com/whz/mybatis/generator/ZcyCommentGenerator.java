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
package com.whz.mybatis.generator;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.util.List;

public class ZcyCommentGenerator extends DefaultCommentGenerator {
    public void addGeneralMethodComment(Method method, IntrospectedTable introspectedTable) {
        method.addJavaDocLine("/**");
        String method_name = method.getName();
        if ("deleteById".equals(method_name)) {
            method.addJavaDocLine(" * 根据主键删除数据");
        } else if ("insert".equals(method_name)) {
            method.addJavaDocLine(" * 插入数据");
        } else if ("insertSelective".equals(method_name)) {
            method.addJavaDocLine(" * 选择字段插入数据");
        } else if ("selectById".equals(method_name)) {
            method.addJavaDocLine(" * 根据主键获取数据");
        } else if ("updateById".equals(method_name)) {
            method.addJavaDocLine(" * 根据主键更新全部字段数据");
        } else if ("updateByIdSelective".equals(method_name)) {
            method.addJavaDocLine(" * 根据主键选择更新字段数据");
        } else if ("query".equals(method_name)) {
            method.addJavaDocLine(" * 根据条件查询数据");
        } else if ("insertBatch".equals(method_name)) {
            method.addJavaDocLine(" * 添加List集合对象所有字段");
        }
        method.addJavaDocLine(" *");
        List<Parameter> parameterList = method.getParameters();
        for (Parameter parameter : parameterList) {
            String paramterName = parameter.getName();
            method.addJavaDocLine(" * @param " + paramterName);
        }
        method.addJavaDocLine(" */");
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        String shortName = innerClass.getType().getShortName();
        innerClass.addJavaDocLine("/**");
        innerClass.addJavaDocLine(" * " + introspectedTable.getRemarks());
        innerClass.addJavaDocLine(" * " + shortName);
        innerClass.addJavaDocLine(" * 数据库表：" + introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(" */");
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        String shortName = innerClass.getType().getShortName();
        innerClass.addJavaDocLine("/**");
        innerClass.addJavaDocLine(" * 类注释");
        innerClass.addJavaDocLine(" * " + shortName);
        innerClass.addJavaDocLine(" * 数据库表：" + introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(" */");
    }

    public void addNoModelClassComment(JavaElement javaElement, IntrospectedTable introspectedTable) {
        javaElement.addJavaDocLine("/**");
        javaElement.addJavaDocLine(" * " + introspectedTable.getRemarks() + "DAO层操作");
        javaElement.addJavaDocLine(" * 数据库表：" + introspectedTable.getFullyQualifiedTable());
        addJavadocTag(javaElement, false);
        javaElement.addJavaDocLine(" */");
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuffer sb = new StringBuffer();
        sb.append("/** ");
        if (introspectedColumn.getRemarks() != null)
            sb.append(introspectedColumn.getRemarks());
        sb.append(" */");
        field.addJavaDocLine(sb.toString());
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        StringBuilder sb = new StringBuilder();
        if ("serialVersionUID".equals(field.getName())) {
            field.addJavaDocLine("");
            sb.append(" * 串行版本ID");
        }
        if (sb.length() > 0)
            field.addJavaDocLine("/** " + sb.toString() + "*/");
    }

    public void addGetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuffer sb = new StringBuffer();
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * <pre>");
        method.addJavaDocLine(" * 获取：" + introspectedColumn.getRemarks());
        sb.append(" * 表字段：");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" * </pre>");
        method.addJavaDocLine(" *");
        sb = new StringBuffer();
        sb.append(" * @return ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        sb.append("：");
        sb.append(introspectedColumn.getRemarks());
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */");
    }

    public void addSetterComment(Method method, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        StringBuffer sb = new StringBuffer();
        method.addJavaDocLine("/**");
        method.addJavaDocLine(" * <pre>");
        method.addJavaDocLine(" * 设置：" + introspectedColumn.getRemarks());
        sb.append(" * 表字段：");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" * </pre>");
        method.addJavaDocLine(" *");
        Parameter parm = method.getParameters().get(0);
        method.addJavaDocLine(" * @param " + parm.getName());
        sb = new StringBuffer();
        sb.append(" *            ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        sb.append('.');
        sb.append(introspectedColumn.getActualColumnName());
        sb.append("：");
        sb.append(introspectedColumn.getRemarks());
        method.addJavaDocLine(sb.toString());
        method.addJavaDocLine(" */");
    }

    public void addComment(XmlElement xmlElement) {
        StringBuilder sb = new StringBuilder();
        xmlElement.addElement((Element) new TextElement(sb.toString()));
    }
}
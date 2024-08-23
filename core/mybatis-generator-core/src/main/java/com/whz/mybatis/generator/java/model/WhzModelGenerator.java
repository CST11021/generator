package com.whz.mybatis.generator.java.model;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.mybatis.generator.internal.util.JavaBeansUtil.*;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @Author 盖伦
 * @Date 2023/5/17
 */
public class WhzModelGenerator extends AbstractJavaGenerator {

    public WhzModelGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.8", introspectedTable.getFullyQualifiedTable().toString()));

        // 创建一个TopLevelClass
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getBaseRecordType());
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);

        // 代码生成类
        CommentGenerator commentGenerator = context.getCommentGenerator();
        commentGenerator.addJavaFileComment(topLevelClass);

        // 获取父类
        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            // 继承父类
            topLevelClass.setSuperClass(superClass);
            // import父类
            topLevelClass.addImportedType(superClass);
        }

        // 给类添加注释
        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);



        if (introspectedTable.isConstructorBased()) {
            // 添加带有参数的构造函数
            addParameterizedConstructor(topLevelClass);

            // 添加默认的构造函数
            if (!introspectedTable.isImmutable()) {
                addDefaultConstructor(topLevelClass);
            }
        }

        // lombok注解
        if (useLombok()) {
            topLevelClass.addAnnotation("@Data");
            topLevelClass.addImportedType("lombok.Data");
        }

        if (generalSerializable()) {
            topLevelClass.addImportedType("java.io.Serializable");
            topLevelClass.addSuperInterface(new FullyQualifiedJavaType("java.io.Serializable"));
            Field field = new Field();
            field.setFinal(true);
            field.setInitializationString(String.valueOf((new Random()).nextLong()) + "L");
            field.setName("serialVersionUID");
            field.setStatic(true);
            field.setType(new FullyQualifiedJavaType("long"));
            field.setVisibility(JavaVisibility.PRIVATE);
            this.context.getCommentGenerator().addFieldComment(field, introspectedTable);

            topLevelClass.addField(field);
        }

        String rootClass = getRootClass();
        Plugin plugins = context.getPlugins();
        for (IntrospectedColumn introspectedColumn : introspectedTable.getAllColumns()) {
            if (RootClassInfo.getInstance(rootClass, warnings).containsProperty(introspectedColumn)) {
                continue;
            }

            // 添加字段
            Field field = getJavaBeansField(introspectedColumn, context, introspectedTable);
            if (plugins.modelFieldGenerated(field, topLevelClass, introspectedColumn, introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                // 添加字段
                topLevelClass.addField(field);
                // 导出字段类型
                topLevelClass.addImportedType(field.getType());
            }

            if (!useLombok()) {
                // 添加getter方法
                Method getterMethod = getJavaBeansGetter(introspectedColumn, context, introspectedTable);
                if (plugins.modelGetterMethodGenerated(getterMethod, topLevelClass, introspectedColumn, introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                    topLevelClass.addMethod(getterMethod);
                }

                // 添加setter方法
                Method setterMethod = getJavaBeansSetter(introspectedColumn, context, introspectedTable);
                if (plugins.modelSetterMethodGenerated(setterMethod, topLevelClass, introspectedColumn, introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                    topLevelClass.addMethod(setterMethod);
                }
            }

        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

    private boolean generalSerializable() {
        String generalSerializable = introspectedTable.getContext().getProperty("generalSerializable");
        return StringUtility.stringHasValue(generalSerializable) && "true".equals(generalSerializable);
    }

    private boolean useLombok() {
        String useLombok = introspectedTable.getContext().getProperty("useLombok");
        return StringUtility.stringHasValue(useLombok) && "true".equals(useLombok);
    }

    /**
     * 返回父类的FullyQualifiedJavaType
     *
     * @return
     */
    private FullyQualifiedJavaType getSuperClass() {
        FullyQualifiedJavaType superClass;
        String rootClass = getRootClass();
        if (rootClass != null) {
            superClass = new FullyQualifiedJavaType(rootClass);
        } else {
            superClass = null;
        }

        return superClass;
    }

    /**
     * 添加构造函数
     *
     * @param topLevelClass
     */
    private void addParameterizedConstructor(TopLevelClass topLevelClass) {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setConstructor(true);
        method.setName(topLevelClass.getType().getShortName());
        context.getCommentGenerator().addGeneralMethodComment(method, introspectedTable);

        List<IntrospectedColumn> constructorColumns = introspectedTable.getAllColumns();

        for (IntrospectedColumn introspectedColumn : constructorColumns) {
            method.addParameter(new Parameter(introspectedColumn.getFullyQualifiedJavaType(), introspectedColumn.getJavaProperty()));
        }

        StringBuilder sb = new StringBuilder();
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            boolean comma = false;
            sb.append("super(");
            for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
                if (comma) {
                    sb.append(", ");
                } else {
                    comma = true;
                }
                sb.append(introspectedColumn.getJavaProperty());
            }
            sb.append(");");
            method.addBodyLine(sb.toString());
        }

        List<IntrospectedColumn> introspectedColumns = introspectedTable.getAllColumns();

        for (IntrospectedColumn introspectedColumn : introspectedColumns) {
            sb.setLength(0);
            sb.append("this.");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(" = ");
            sb.append(introspectedColumn.getJavaProperty());
            sb.append(';');
            method.addBodyLine(sb.toString());
        }

        topLevelClass.addMethod(method);
    }
}
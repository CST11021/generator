package com.whz.mybatis.generator.java.model;

import com.whz.mybatis.generator.api.IntrospectedColumnForQuery;
import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.mybatis.generator.internal.util.JavaBeansUtil.*;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @Author 盖伦
 * @Date 2023/5/17
 */
public class WhzModelQcGenerator extends AbstractJavaGenerator {

    public WhzModelQcGenerator() {
        super();
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        progressCallback.startTask(getString("Progress.8", introspectedTable.getFullyQualifiedTable().toString()));

        // 创建一个TopLevelClass
        TopLevelClass topLevelClass = createTopLevelClass();
        // 类设置为public
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        // 给类添加注释
        doCommentGenerate(topLevelClass);
        // 设置继承父类
        setExtendParentClassIfNecessary(topLevelClass);
        // 设置构造函数
        setConstructor(topLevelClass);
        // 设置lombok注解
        setLombokAnnotationIfNecessary(topLevelClass);


        // 获取要生成的查询字段
        List<IntrospectedColumnForQuery> queryColumns = introspectedTable.getQueryColumns();
        // 遍历配置的查询字段
        for (IntrospectedColumnForQuery query : queryColumns) {

            // 设置生成的字段
            setField(topLevelClass, query);

            // 生成getter和setter方法
            setGetterAndSetterIfNecessary(topLevelClass, query);
        }

        List<CompilationUnit> answer = new ArrayList<CompilationUnit>();
        if (context.getPlugins().modelBaseRecordClassGenerated(topLevelClass, introspectedTable)) {
            answer.add(topLevelClass);
        }
        return answer;
    }

    /**
     * 创建一个TopLevelClass
     *
     * @return
     */
    private TopLevelClass createTopLevelClass() {
        FullyQualifiedJavaType type = new FullyQualifiedJavaType(introspectedTable.getQueryRecordType());
        return new TopLevelClass(type);
    }

    /**
     * 设置继承父类
     *
     * @param topLevelClass
     */
    private void setExtendParentClassIfNecessary(TopLevelClass topLevelClass) {
        // 获取父类
        FullyQualifiedJavaType superClass = getSuperClass();
        if (superClass != null) {
            // 继承父类
            topLevelClass.setSuperClass(superClass);
            // import父类
            topLevelClass.addImportedType(superClass);
        }
    }

    /**
     * 设置lombok注解
     *
     * @param topLevelClass
     */
    private void setLombokAnnotationIfNecessary(TopLevelClass topLevelClass) {
        // lombok注解
        if (useLombok()) {
            topLevelClass.addAnnotation("@Data");
            topLevelClass.addImportedType("lombok.Data");
        }
    }

    /**
     * 设置生成的字段
     *
     * @param topLevelClass
     * @param query
     */
    private void setField(TopLevelClass topLevelClass, IntrospectedColumnForQuery query) {
        Plugin plugins = context.getPlugins();


        // 添加字段
        Field field = getJavaBeansField(query, context, introspectedTable);
        if (plugins.modelFieldGenerated(field, topLevelClass, query.getIntrospectedColumn(), introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
            // 添加字段
            topLevelClass.addField(field);
            // 导出字段类型
            topLevelClass.addImportedType(field.getType());
            if (query.isArray()) {
                topLevelClass.addImportedType(FullyQualifiedJavaType.getNewListInstance());
            }
        }
    }
    /**
     * 设置getter和Setter方法
     *
     * @param topLevelClass
     * @param query
     */
    private void setGetterAndSetterIfNecessary(TopLevelClass topLevelClass, IntrospectedColumnForQuery query) {
        Plugin plugins = context.getPlugins();
        // 不使用@Lombok注解的，生成getter和setter方法
        if (!useLombok()) {
            // 添加getter方法
            Method getterMethod = getJavaBeansGetter(query, context, introspectedTable);
            if (plugins.modelGetterMethodGenerated(getterMethod, topLevelClass, query.getIntrospectedColumn(), introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addMethod(getterMethod);
            }

            // 添加setter方法
            Method setterMethod = getJavaBeansSetter(query, context, introspectedTable);
            if (plugins.modelSetterMethodGenerated(setterMethod, topLevelClass, query.getIntrospectedColumn(), introspectedTable, Plugin.ModelClassType.BASE_RECORD)) {
                topLevelClass.addMethod(setterMethod);
            }
        }
    }

    /**
     * 设置构造函数
     * @param topLevelClass
     */
    private void setConstructor(TopLevelClass topLevelClass) {
        if (introspectedTable.isConstructorBased()) {
            // 添加带有参数的构造函数
            addParameterizedConstructor(topLevelClass);

            // 添加默认的构造函数
            if (!introspectedTable.isImmutable()) {
                addDefaultConstructor(topLevelClass);
            }
        }
    }

    /**
     * 给类添加注释
     *
     * @param topLevelClass
     */
    private void doCommentGenerate(TopLevelClass topLevelClass) {
        // 代码生成类
        CommentGenerator commentGenerator = context.getCommentGenerator();
        // 设置需要生成注释的类
        commentGenerator.addJavaFileComment(topLevelClass);
        // 给类添加注释
        commentGenerator.addModelClassComment(topLevelClass, introspectedTable);
    }

    /**
     * 返回父类的FullyQualifiedJavaType
     *
     * @return
     */
    private FullyQualifiedJavaType getSuperClass() {
        Properties properties = context.getJavaQueryModelGeneratorConfiguration().getProperties();
        String superClass = properties.getProperty(PropertyRegistry.ANY_ROOT_CLASS);
        if (!StringUtility.stringHasValue(superClass)) {
            return null;
        }
        return new FullyQualifiedJavaType(superClass);
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

    /**
     * 是否使用lombok注解
     *
     * @return
     */
    private boolean useLombok() {
        String useLombok = introspectedTable.getContext().getProperty("useLombok");
        return StringUtility.stringHasValue(useLombok) && "true".equals(useLombok);
    }
}

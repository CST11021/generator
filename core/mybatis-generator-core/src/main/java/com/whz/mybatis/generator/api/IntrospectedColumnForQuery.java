package com.whz.mybatis.generator.api;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * @Author 盖伦
 * @Date 2023/9/8
 */
public class IntrospectedColumnForQuery {

    /** 是否是集合类型的查询字段 */
    private boolean isArray;

    /** 查询字段名 */
    private String fieldName;

    /** 查询的字段类型 */
    private FullyQualifiedJavaType javaType;

    /** 查询字段对应的DB字段元数据 */
    private IntrospectedColumn introspectedColumn;

    public IntrospectedColumnForQuery(boolean isArray, IntrospectedColumn introspectedColumn) {
        if (isArray) {
            javaType = FullyQualifiedJavaType.getNewListInstance();
            javaType.addTypeArgument(introspectedColumn.getFullyQualifiedJavaType());
            fieldName = introspectedColumn.getJavaProperty() + "List";
        } else {
            javaType = introspectedColumn.getFullyQualifiedJavaType();
            fieldName = introspectedColumn.getJavaProperty();
        }
        this.isArray = isArray;
        this.introspectedColumn = introspectedColumn;
    }

    public boolean isArray() {
        return isArray;
    }

    public void setArray(boolean array) {
        isArray = array;
    }

    public IntrospectedColumn getIntrospectedColumn() {
        return introspectedColumn;
    }

    public void setIntrospectedColumn(IntrospectedColumn introspectedColumn) {
        this.introspectedColumn = introspectedColumn;
    }

    public FullyQualifiedJavaType getJavaType() {
        return javaType;
    }

    public void setJavaType(FullyQualifiedJavaType javaType) {
        this.javaType = javaType;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }
}

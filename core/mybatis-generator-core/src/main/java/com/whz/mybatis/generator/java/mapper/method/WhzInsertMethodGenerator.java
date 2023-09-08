package com.whz.mybatis.generator.java.mapper.method;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.Set;
import java.util.TreeSet;

/**
 * @Author 盖伦
 * @Date 2023/5/17
 */
public class WhzInsertMethodGenerator extends AbstractJavaMapperMethodGenerator {

    public WhzInsertMethodGenerator() {
        super();
    }

    /**
     * int batchInsert(List<T> doList);
     *
     * @param interfaze
     */
    @Override
    public void addInterfaceElements(Interface interfaze) {
        Method method = buildMethod();
        interfaze.addMethod(method);
        interfaze.addImportedTypes(buildImportedTypes());

        // 添加注释
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
    }

    /**
     * build Method
     *
     * @return
     */
    private Method buildMethod() {
        Method method = new Method();
        method.setReturnType(new FullyQualifiedJavaType("int"));
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("insert");
        method.addParameter(buildParameter());

        return method;
    }

    /**
     * java.util.List<whz.ZlbBaidaForm>
     *
     * @return
     */
    private Parameter buildParameter() {
        return new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()), "DO");
    }

    /**
     * int insert(T baseDO);
     *
     * @return
     */
    private Set<FullyQualifiedJavaType> buildImportedTypes() {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet();
        importedTypes.add(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()));

        return importedTypes;
    }
}

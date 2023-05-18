package com.whz.mybatis.generator.java.mapper.method;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.Set;
import java.util.TreeSet;

/**
 * @Author 盖伦
 * @Date 2023/5/17
 */
public class WhzList extends AbstractJavaMapperMethodGenerator {

    public WhzList() {
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
        this.context.getCommentGenerator().addGeneralMethodComment(method, this.introspectedTable);
    }

    /**
     * build Method
     *
     * @return
     */
    private Method buildMethod() {
        Method method = new Method();
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setReturnType(buildReturnType());
        method.setName("list");
        method.addParameter(buildParameter());

        return method;
    }

    private FullyQualifiedJavaType buildReturnType() {
        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        listType.addTypeArgument(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()));

        return listType;
    }

    /**
     * java.util.List<whz.ZlbBaidaForm>
     *
     * @return
     */
    private Parameter buildParameter() {
        return new Parameter(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType() + "QC"), "qc");
    }

    private Set<FullyQualifiedJavaType> buildImportedTypes() {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        importedTypes.add(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()));

        return importedTypes;
    }
}

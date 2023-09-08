package com.whz.mybatis.generator.java.mapper.method;

import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.Set;
import java.util.TreeSet;

/**
 * @Author 盖伦
 * @Date 2023/5/17
 */
public class WhzBatchInsertMethodGenerator extends AbstractJavaMapperMethodGenerator {

    public WhzBatchInsertMethodGenerator() {
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
        method.setReturnType(FullyQualifiedJavaType.getIntInstance());
        method.setVisibility(JavaVisibility.PUBLIC);
        method.setName("batchInsert");
        method.addParameter(buildParameter());

        return method;
    }

    /**
     * java.util.List<whz.ZlbBaidaForm>
     *
     * @return
     */
    private Parameter buildParameter() {
        FullyQualifiedJavaType listType = FullyQualifiedJavaType.getNewListInstance();
        listType.addTypeArgument(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()));
        return new Parameter(listType, "doList");
    }

    private Set<FullyQualifiedJavaType> buildImportedTypes() {
        Set<FullyQualifiedJavaType> importedTypes = new TreeSet();
        importedTypes.add(FullyQualifiedJavaType.getNewListInstance());
        importedTypes.add(new FullyQualifiedJavaType(this.introspectedTable.getBaseRecordType()));

        return importedTypes;
    }

}

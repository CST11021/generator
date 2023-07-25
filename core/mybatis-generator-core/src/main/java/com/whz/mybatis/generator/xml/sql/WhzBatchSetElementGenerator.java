package com.whz.mybatis.generator.xml.sql;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzBatchSetElementGenerator extends AbstractXmlElementGenerator {


    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "batchSet"));

        for (IntrospectedColumn column : introspectedTable.getTableAllColumns()) {
            if (column.isAutoIncrement()) {
                continue;
            }

            XmlElement ifE = new XmlElement("if");
            ifE.addAttribute(new Attribute("test", "item." + column.getJavaProperty() + " != null"));
            ifE.addElement(new TextElement(column.getActualColumnName() + " = #{item." + column.getJavaProperty() + "}, "));
            answer.addElement(ifE);
        }

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

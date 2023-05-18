package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Iterator;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzBatchSetElementGenerator extends AbstractXmlElementGenerator {


    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "batchSet"));

        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            IntrospectedColumn column = iter.next();

            XmlElement ifE = new XmlElement("if");
            ifE.addAttribute(new Attribute("test", "item." + column.getJavaProperty() + " != null"));
            ifE.addElement(new TextElement("and " + column.getActualColumnName() + " = #{item." + column.getJavaProperty() + "}"));
            answer.addElement(ifE);
        }

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

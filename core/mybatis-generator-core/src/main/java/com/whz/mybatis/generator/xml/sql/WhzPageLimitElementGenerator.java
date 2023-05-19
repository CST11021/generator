package com.whz.mybatis.generator.xml.sql;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzPageLimitElementGenerator extends AbstractXmlElementGenerator {

    @Override
    public void addElements(XmlElement parentElement) {
        String offsetFieldName = introspectedTable.getTableConfiguration().getOffsetFieldName();
        String limitFieldName = introspectedTable.getTableConfiguration().getLimitFieldName();
        if (!StringUtility.stringHasValue(offsetFieldName) || !StringUtility.stringHasValue(limitFieldName)) {
            return;
        }

        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "page-limit"));

        XmlElement ifE = new XmlElement("if");
        ifE.addAttribute(new Attribute("test", offsetFieldName + " != null and " + limitFieldName +" != null"));
        ifE.addElement(new TextElement("LIMIT #{" + offsetFieldName + "}, #{"+ limitFieldName +"}"));
        answer.addElement(ifE);

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

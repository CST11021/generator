package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 *
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzTableNameElementGenerator extends AbstractXmlElementGenerator {

    /**
     * <sql id="tableName">production_device</sql>
     *
     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "tableName"));
        answer.addElement(new TextElement(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        parentElement.addElement(answer);

        context.getCommentGenerator().addComment(answer);
    }






}

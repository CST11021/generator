package com.whz.mybatis.generator.xml.sql;

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
public class WhzBaseColumnElementGenerator extends AbstractXmlElementGenerator {

    // <sql id="baseColumn">
    //         <![CDATA[
    // 			id,
    // 			device_code,
    // 			device_name,
    // 			total,
    // 			creator,
    // 			modifier,
    // 			gmt_create,
    // 			gmt_modified,
    // 			is_deleted]]>
    //     </sql>
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "baseColumn"));

        answer.addElement(new TextElement("<![CDATA["));

        StringBuilder sb = new StringBuilder();
        Iterator<IntrospectedColumn> iter = introspectedTable.getTableAllColumns().iterator();
        while (iter.hasNext()) {
            sb.append(iter.next().getActualColumnName());
            if (iter.hasNext()) {
                sb.append(", ");
            }
        }
        answer.addElement(new TextElement(sb.toString()));

        answer.addElement(new TextElement("]]>"));

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

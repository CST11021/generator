package com.whz.mybatis.generator.xml;

import org.apache.tools.ant.util.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @Author 盖伦
 * @Date 2023/5/17
 */
public class WhzBatchSetValueElementGenerator extends AbstractXmlElementGenerator {

    /**
     * <sql id="batchSetValue">
     *     #{item.id},
     *     #{item.code},
     *     #{item.name},
     *     #{item.description},
     *     #{item.belong},
     *     #{item.tenantCode},
     *     #{item.orgId},
     *     #{item.creator},
     *     #{item.modifier},
     *     #{item.rAddTime},
     *     #{item.rModifiedTime},
     *     #{item.isDelete},
     *     #{item.pcScheme},
     *     #{item.appScheme}
     *   </sql>
     *
     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "batchSetValue"));

        StringBuilder sb = new StringBuilder();
        for (IntrospectedColumn column : introspectedTable.getAllColumns()) {
            sb.append("#{item." + column.getJavaProperty() + "},\n");
        }
        StringUtils.removeSuffix(sb.toString(), ",");

        answer.addElement(new TextElement(sb.toString()));
        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

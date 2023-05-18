package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzPageLimitElementGenerator extends AbstractXmlElementGenerator {

    String offsetFieldName;
    String limitFieldName;
    //     <sql id="page-limit">
    //         <if test="offset !=null and limit!=null">
    //             LIMIT #{offset}, #{limit}
    //         </if>
    //     </sql>
    @Override
    public void addElements(XmlElement parentElement) {
        if (offsetFieldName == null) {
            offsetFieldName = "offset";
        }

        if (limitFieldName == null) {
            limitFieldName = "limit";
        }

        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "page-limit"));

        XmlElement ifE = new XmlElement("if");
        ifE.addAttribute(new Attribute("test", offsetFieldName + " !=null and " + limitFieldName +" !=null"));
        ifE.addElement(new TextElement("LIMIT #{" + offsetFieldName + "}, #{"+ limitFieldName +"}"));
        answer.addElement(ifE);

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzCountElementGenerator extends AbstractXmlElementGenerator {

    /**
     *     <select id="count" parameterType="com.tuya.hecate.core.entity.device.DeviceQC" resultType="java.lang.Long">
     *         select count(1)
     *         from <include refid="tableName"/>
     *         <where>
     *             <include refid="qc"/>
     *         </where>
     *     </select>
     *
     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "count"));
        answer.addAttribute(new Attribute("parameterType",  introspectedTable.getBaseRecordType()));
        answer.addAttribute(new Attribute("resultType",  "java.lang.Long"));

        answer.addElement(new TextElement("select count(1)\n" +
                "        from <include refid=\"tableName\"/>\n" +
                "        <where>\n" +
                "            <include refid=\"qc\"/>\n" +
                "        </where>"));

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

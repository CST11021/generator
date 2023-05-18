package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzInsertElementGenerator extends AbstractXmlElementGenerator {

    /**
     *      <insert id="insert" parameterType="com.tuya.hecate.core.entity.device.DeviceDO">
     *         INSERT INTO <include refid="tableName"/>
     *         <set>
     *             <include refid="set"/>
     *         </set>
     *     </insert>
     *
     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", "insert"));
        answer.addAttribute(new Attribute("parameterType",  introspectedTable.getBaseRecordType()));

        answer.addElement(new TextElement("INSERT INTO <include refid=\"tableName\"/>\n" +
                "        <set>\n" +
                "            <include refid=\"set\"/>\n" +
                "        </set>"));

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}
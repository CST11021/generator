package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzUpdateElementGenerator extends AbstractXmlElementGenerator {

    /**
     *     <update id="update" parameterType="com.tuya.hecate.core.entity.device.DeviceDO">
     *         UPDATE <include refid="tableName"/>
     *         <set>
     *             <include refid="set"/>
     *         </set>
     *         where id = #{id}
     *     </update>
     *
     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement e = new XmlElement("update");
        e.addAttribute(new Attribute("id", "update"));
        e.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));
        e.addElement(new TextElement("UPDATE <include refid=\"tableName\"/>"));
        e.addElement(new TextElement("<set>"));
        e.addElement(new TextElement("<include refid=\"batchSet\"/>"));
        e.addElement(new TextElement("</set>"));
        e.addElement(new TextElement("WHERE id = #{id}"));

        parentElement.addElement(e);
        context.getCommentGenerator().addComment(e);
    }






}

package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzListElementGenerator extends AbstractXmlElementGenerator {

    /**
     *     <select id="list" parameterType="com.tuya.hecate.core.entity.device.DeviceQC" resultMap="BaseResultMap">
     *         select
     *         <include refid="baseColumn"/>
     *         from <include refid="tableName"/>
     *         <where>
     *             <include refid="qc"/>
     *         </where>
     *         <if test="orderBy != null">
     *             order by ${orderBy}
     *         </if>
     *         <include refid="page-limit"/>
     *     </select>
     *
     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("select");
        answer.addAttribute(new Attribute("id", "list"));
        answer.addAttribute(new Attribute("parameterType",  introspectedTable.getBaseRecordType()));
        answer.addAttribute(new Attribute("resultMap",  "BaseResultMap"));

        answer.addElement(new TextElement("select\n" +
                "        <include refid=\"baseColumn\"/>\n" +
                "        from <include refid=\"tableName\"/>\n" +
                "        <where>\n" +
                "            <include refid=\"qc\"/>\n" +
                "        </where>\n" +
                "        <if test=\"orderBy != null\">\n" +
                "            order by ${orderBy}\n" +
                "        </if>\n" +
                "        <include refid=\"page-limit\"/>"));

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

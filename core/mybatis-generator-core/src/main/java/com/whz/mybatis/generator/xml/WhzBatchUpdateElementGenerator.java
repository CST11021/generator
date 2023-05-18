package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzBatchUpdateElementGenerator extends AbstractXmlElementGenerator {

    /**
     *     <update id="batchUpdate">
     *         <foreach collection="list" item="item" index="index" separator=";">
     *             UPDATE <include refid="tableName"/>
     *             <set>
     *                 <include refid="batchSet"/>
     *             </set>
     *             WHERE id = #{item.id}
     *         </foreach>
     *     </update>
     *
     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("update");
        answer.addAttribute(new Attribute("id", "batchUpdate"));

        answer.addElement(new TextElement("<foreach collection=\"list\" item=\"item\" index=\"index\" separator=\";\">\n" +
                "            UPDATE <include refid=\"tableName\"/>\n" +
                "            <set>\n" +
                "                <include refid=\"batchSet\"/>\n" +
                "            </set>\n" +
                "            WHERE id = #{item.id}\n" +
                "        </foreach>"));

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

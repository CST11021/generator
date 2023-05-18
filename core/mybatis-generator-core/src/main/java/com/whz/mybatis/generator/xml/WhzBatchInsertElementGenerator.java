package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.List;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzBatchInsertElementGenerator extends AbstractXmlElementGenerator {

    List<String> includeColumnNameList;
    List<String> excludeColumnNameList;
    String deleteColumnName = "";
    String undeleteValue = "";
    String deletedValue = "";

    /**
     *     insert into
     *         <include refid="tableName"/>(<include refid="baseColumn"/>)
     *     values
     *         <foreach collection="list" index="index" item="item" separator=",">
     *           (<include refid="batchSetValue"/>)
     *         </foreach>
     *
     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("insert");
        answer.addAttribute(new Attribute("id", "batchInsert"));
        answer.addAttribute(new Attribute("parameterType", introspectedTable.getBaseRecordType()));

        answer.addElement(new TextElement("    insert into \n" +
                "        <include refid=\"tableName\"/>(<include refid=\"baseColumn\"/>)\n" +
                "    values\n" +
                "        <foreach collection=\"list\" index=\"index\" item=\"item\" separator=\",\">\n" +
                "          (<include refid=\"batchSetValue\"/>)\n" +
                "        </foreach>"));

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}
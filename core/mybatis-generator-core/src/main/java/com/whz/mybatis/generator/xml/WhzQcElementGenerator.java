package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import java.util.Iterator;
import java.util.List;

/**
 * @Author 盖伦
 * @Date 2023/5/16
 */
public class WhzQcElementGenerator extends AbstractXmlElementGenerator {

    //     <!-- 查询条件 -->
    //     <sql id="qc">
    //         <if test="id != null">
    //             and `id` = #{id}
    //         </if>
    //         <if test="deviceCode != null">
    //             and `device_code` = #{deviceCode}
    //         </if>
    //         <if test="deviceName != null">
    //             and `device_name` = #{deviceName}
    //         </if>
    //         <if test="total != null">
    //             and `total` = #{total}
    //         </if>
    //         <if test="creator != null">
    //             and `creator` = #{creator}
    //         </if>
    //         <if test="modifier != null">
    //             and `modifier` = #{modifier}
    //         </if>
    //     </sql>
    List<String> includeColumnNameList;
    List<String> excludeColumnNameList;
    String deleteColumnName = "";
    String undeleteValue = "";
    String deletedValue = "";

    @Override
    public void addElements(XmlElement parentElement) {
        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "qc"));

        Iterator<IntrospectedColumn> iter = introspectedTable.getAllColumns().iterator();
        while (iter.hasNext()) {
            IntrospectedColumn column = iter.next();

            if (column.getActualColumnName().equals(deleteColumnName)) {
                answer.addElement(new TextElement("deleteColumnName = " + undeleteValue));
            }

            if (excludeColumnNameList != null && excludeColumnNameList.contains(column.getActualColumnName())) {
                continue;
            }

            if (includeColumnNameList == null || includeColumnNameList.contains(column.getActualColumnName())) {
                XmlElement ifE = new XmlElement("if");
                ifE.addAttribute(new Attribute("test", column.getJavaProperty() + " != null"));
                ifE.addElement(new TextElement("and " + column.getActualColumnName() + " = #{" + column.getJavaProperty() + "}"));
                answer.addElement(ifE);
            }
        }

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

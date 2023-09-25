package com.whz.mybatis.generator.xml.sql;

import com.whz.mybatis.generator.api.IntrospectedColumnForQuery;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

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

    String undeleteValue = "";
    String deletedValue = "";

    @Override
    public void addElements(XmlElement parentElement) {
        String deleteColumnName = introspectedTable.getTableConfiguration().getDeleteColumnName();
        String undeleteValue = introspectedTable.getTableConfiguration().getUndeleteValue();

        XmlElement answer = new XmlElement("sql");
        answer.addAttribute(new Attribute("id", "qc"));

        for (IntrospectedColumn column : introspectedTable.getTableAllColumns()) {

            if (column.getActualColumnName().equals(deleteColumnName)) {
                answer.addElement(0, new TextElement(column.getActualColumnName() + " = " + undeleteValue));
            }



        }

        for (IntrospectedColumnForQuery query : introspectedTable.getQueryColumns()) {
            IntrospectedColumn column = query.getIntrospectedColumn();
            if (query.isArray()) {
                // <if test="typeList != null">
                //     and type in
                //     <foreach collection="typeList" item="item" open="(" close=")" separator=",">
                //         #{item}
                //     </foreach>
                // </if>
                XmlElement ifE = new XmlElement("if");
                ifE.addAttribute(new Attribute("test", query.getFieldName() + " != null"));
                ifE.addElement(new TextElement("and " + column.getActualColumnName() + " in"));
                ifE.addElement(buildForeachElement(query));
                answer.addElement(ifE);

            } else {
                XmlElement ifE = new XmlElement("if");
                ifE.addAttribute(new Attribute("test", column.getJavaProperty() + " != null"));
                ifE.addElement(new TextElement("and " + column.getActualColumnName() + " = #{" + column.getJavaProperty() + "}"));
                answer.addElement(ifE);
            }
        }

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

    private XmlElement buildForeachElement(IntrospectedColumnForQuery query) {
        XmlElement foreachElement = new XmlElement("foreach");
        foreachElement.addAttribute(new Attribute("collection", query.getFieldName()));
        foreachElement.addAttribute(new Attribute("item", "item"));
        foreachElement.addAttribute(new Attribute("open", "("));
        foreachElement.addAttribute(new Attribute("close", ")"));
        foreachElement.addAttribute(new Attribute("separator", ","));
        foreachElement.addElement(new TextElement("#{item}"));
        return foreachElement;
    }

}

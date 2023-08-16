package com.whz.mybatis.generator.xml.sql;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import static com.whz.mybatis.generator.config.WhzConstant.BATCH_INSERT_VALUE;

/**
 * @Author 盖伦
 * @Date 2023/5/17
 */
public class WhzBatchInsertValueElementGenerator extends AbstractXmlElementGenerator {

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
        answer.addAttribute(new Attribute("id", BATCH_INSERT_VALUE));

        answer.addElement(buildTrimElement());

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

    /**
     * <trim suffix="," suffixOverrides=""></trim>
     *
     * @return
     */
    private XmlElement buildTrimElement() {
        XmlElement trimElement = new XmlElement("trim");
        trimElement.addAttribute(new Attribute("suffix", ""));
        trimElement.addAttribute(new Attribute("suffixOverrides", ","));

        for (IntrospectedColumn column : introspectedTable.getTableAllColumns()) {
            // now(),
            String gmtCreateColumn = column.getIntrospectedTable().getTableConfiguration().getGmtCreateColumn();
            if (column.getActualColumnName().equals(gmtCreateColumn)) {
                trimElement.addElement(buildChooseElement(column.getJavaProperty(), "now()"));
                continue;
            }

            // now(),
            String gmtModifiedColumn = column.getIntrospectedTable().getTableConfiguration().getGmtModifiedColumn();
            if (column.getActualColumnName().equals(gmtModifiedColumn)) {
                trimElement.addElement(buildChooseElement(column.getJavaProperty(), "now()"));
                continue;
            }

            // 0,
            String deleteColumn = column.getIntrospectedTable().getTableConfiguration().getDeleteColumnName();
            String undeleteValue = column.getIntrospectedTable().getTableConfiguration().getUndeleteValue();
            if (column.getActualColumnName().equals(deleteColumn)) {
                trimElement.addElement(buildChooseElement(column.getJavaProperty(), undeleteValue));
                // trimElement.addElement(new TextElement(undeleteValue + ","));
                continue;
            }

            trimElement.addElement(new TextElement("#{item." + column.getJavaProperty() + "},"));
        }
        return trimElement;
    }

    /**
     * 例如：
     <choose>
         <when test="item.addTime != null">
            #{item.addTime},
         </when>
         <otherwise>
            now(),
         </otherwise>
     </choose>

     * @return
     */
    private XmlElement buildChooseElement(String javaProperty, String value) {
        XmlElement whenElement = new XmlElement("when");
        whenElement.addAttribute(new Attribute("test", "item." + javaProperty + " != null"));
        whenElement.addElement(new TextElement("#{tiem." + javaProperty + "},"));

        XmlElement otherwiseElement = new XmlElement("otherwise");
        otherwiseElement.addElement(new TextElement(value + ","));

        XmlElement chooseElement = new XmlElement("choose");
        chooseElement.addElement(whenElement);
        chooseElement.addElement(otherwiseElement);

        return chooseElement;
    }

}

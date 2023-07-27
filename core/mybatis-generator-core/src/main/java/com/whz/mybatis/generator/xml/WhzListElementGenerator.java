package com.whz.mybatis.generator.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import org.mybatis.generator.internal.util.StringUtility;

import static com.whz.mybatis.generator.config.WhzConstant.NEW_LINE;
import static com.whz.mybatis.generator.config.WhzConstant.TAB;

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
     *             order by #{orderBy}
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
        answer.addAttribute(new Attribute("parameterType",  introspectedTable.getQueryRecordType()));
        answer.addAttribute(new Attribute("resultMap",  "BaseResultMap"));

        StringBuffer sb = new StringBuffer();
        sb.append("select <include refid=\"baseColumn\"/>" +
                NEW_LINE + TAB + "from <include refid=\"tableName\"/>" +
                NEW_LINE + TAB + "<where>" +
                NEW_LINE + TAB + TAB + "<include refid=\"qc\"/>" +
                NEW_LINE + TAB + "</where>");

        // 启用排序
        if (introspectedTable.getTableConfiguration().isEnableGeneralOrderBy()) {
            String orderByFieldName = introspectedTable.getTableConfiguration().getOrderByFieldName();
            if (StringUtility.stringHasValue(orderByFieldName)) {
                sb.append(NEW_LINE + TAB + "<if test=\"" + orderByFieldName + " != null\">" +
                        NEW_LINE + TAB + TAB + "order by #{" + orderByFieldName + "}" +
                        NEW_LINE + TAB + "</if>");
            }
        }

        // 启用分页
        if (introspectedTable.getTableConfiguration().isEnableGeneralOffsetLimit()) {
            String offsetFieldName = introspectedTable.getTableConfiguration().getOffsetFieldName();
            String limitFieldName = introspectedTable.getTableConfiguration().getLimitFieldName();
            if (StringUtility.stringHasValue(offsetFieldName) && StringUtility.stringHasValue(limitFieldName)) {
                sb.append(NEW_LINE + TAB + "<include refid=\"page-limit\"/>");
            }
        }
        answer.addElement(new TextElement(sb.toString()));

        parentElement.addElement(answer);
        context.getCommentGenerator().addComment(answer);
    }

}

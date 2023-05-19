/**
 *    Copyright 2006-2016 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.whz.mybatis.generator.xml.resultmap;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class WhzResultMapElementGenerator extends AbstractXmlElementGenerator {

    private boolean isSimple;

    public WhzResultMapElementGenerator(boolean isSimple) {
        super();
        this.isSimple = isSimple;
    }

    /**
     <resultMap id="BaseResultMap" type="whz.ZlbBaidaForm">
        <id column="id" jdbcType="BIGINT" property="id" />
         <result column="code" jdbcType="VARCHAR" property="code" />
         <result column="name" jdbcType="VARCHAR" property="name" />
         <result column="description" jdbcType="VARCHAR" property="description" />
         <result column="belong" jdbcType="VARCHAR" property="belong" />
         <result column="tenant_code" jdbcType="VARCHAR" property="tenantCode" />
         <result column="org_id" jdbcType="BIGINT" property="orgId" />
         <result column="creator" jdbcType="BIGINT" property="creator" />
         <result column="modifier" jdbcType="BIGINT" property="modifier" />
         <result column="r_add_time" jdbcType="TIMESTAMP" property="rAddTime" />
         <result column="r_modified_time" jdbcType="TIMESTAMP" property="rModifiedTime" />
         <result column="is_delete" jdbcType="TINYINT" property="isDelete" />
     </resultMap>

     <resultMap extends="BaseResultMap" id="ResultMapWithBLOBs" type="whz.ZlbBaidaFormWithBLOBs">
         <result column="pc_scheme" jdbcType="LONGVARCHAR" property="pcScheme" />
         <result column="app_scheme" jdbcType="LONGVARCHAR" property="appScheme" />
     </resultMap>

     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        // 创建<resultMap>标签
        XmlElement answer = new XmlElement("resultMap");
        // 添加id属性
        answer.addAttribute(new Attribute("id", introspectedTable.getBaseResultMapId()));
        // 添加type属性
        answer.addAttribute(new Attribute("type", introspectedTable.getBaseRecordType()));

        context.getCommentGenerator().addComment(answer);

        // 创建多个<id>标签和多个<result>标签
        addResultMapElements(answer);

        parentElement.addElement(answer);
    }

    /**
     * 创建<resultMap>的子标签，它可以包括多个<id>标签和多个<result>标签
     *
     * @param answer
     */
    private void addResultMapElements(XmlElement answer) {

        // 创建<result>标签
        for (IntrospectedColumn introspectedColumn : introspectedTable.getTableAllColumns()) {
            if (isPKey(introspectedColumn)) {
                XmlElement resultElement = new XmlElement("id");
                resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
                resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
                answer.addElement(resultElement);
            } else {
                XmlElement resultElement = new XmlElement("result");
                resultElement.addAttribute(new Attribute("column", MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn)));
                resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty()));
                answer.addElement(resultElement);
            }

        }
    }

    private boolean isPKey(IntrospectedColumn column) {
        for (IntrospectedColumn c : introspectedTable.getPrimaryKeyColumns()) {
            if (c.getActualColumnName().equals(column.getActualColumnName())) {
                return true;
            }
        }

        return false;
    }

}

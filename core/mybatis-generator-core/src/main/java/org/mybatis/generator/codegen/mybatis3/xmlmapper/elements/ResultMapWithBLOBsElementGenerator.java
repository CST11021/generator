/*
 *    Copyright 2006-2023 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;


public class ResultMapWithBLOBsElementGenerator extends AbstractXmlElementGenerator {

    public ResultMapWithBLOBsElementGenerator() {
        super();
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
         <result column="pc_scheme" jdbcType="LONGVARCHAR" property="pcScheme" />
         <result column="app_scheme" jdbcType="LONGVARCHAR" property="appScheme" />
     </resultMap>
     * @param parentElement
     */
    @Override
    public void addElements(XmlElement parentElement) {
        // 1 创建<resultMap>标签
        XmlElement answer = new XmlElement("resultMap");

        // 2 创建id属性
        answer.addAttribute(new Attribute("id", introspectedTable.getResultMapWithBLOBsId()));

        String returnType;
        if (introspectedTable.getRules().generateRecordWithBLOBsClass()) {
            returnType = introspectedTable.getRecordWithBLOBsType();
        } else {
            // table has BLOBs, but no BLOB class - BLOB fields must be in the base class
            returnType = introspectedTable.getBaseRecordType();
        }

        // 3 创建type属性
        answer.addAttribute(new Attribute("type", returnType));

        // 4 创建extends属性
        if (!introspectedTable.isConstructorBased()) {
            answer.addAttribute(new Attribute("extends", introspectedTable.getBaseResultMapId()));
        }

        context.getCommentGenerator().addComment(answer);

        if (introspectedTable.isConstructorBased()) {
            // 创建<constructor>标签
            addResultMapConstructorElements(answer);
        } else {
            // 创建<result>标签
            addResultMapElements(answer);
        }

        if (context.getPlugins().sqlMapResultMapWithBLOBsElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    /**
     * 创建<result>标签
     *
     * @param answer
     */
    private void addResultMapElements(XmlElement answer) {
        buildResultMapItems(ResultElementType.RESULT, introspectedTable.getBLOBColumns()).forEach(answer::addElement);
    }

    /**
     * 创建<constructor>标签
     *
     * @param answer
     */
    private void addResultMapConstructorElements(XmlElement answer) {
        answer.addElement(buildConstructorElement(true));
    }
}

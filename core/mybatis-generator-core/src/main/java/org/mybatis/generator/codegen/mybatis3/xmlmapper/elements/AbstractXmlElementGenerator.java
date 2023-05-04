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

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import org.mybatis.generator.config.GeneratedKey;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

public abstract class AbstractXmlElementGenerator extends AbstractGenerator {
    public abstract void addElements(XmlElement parentElement);

    protected AbstractXmlElementGenerator() {
        super();
    }

    /**
     * This method should return an XmlElement for the select key used to
     * automatically generate keys.
     *
     * @param introspectedColumn
     *            the column related to the select key statement
     * @param generatedKey
     *            the generated key for the current table
     * @return the selectKey element
     */
    protected XmlElement getSelectKey(IntrospectedColumn introspectedColumn,
            GeneratedKey generatedKey) {
        String identityColumnType = introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName();

        XmlElement answer = new XmlElement("selectKey"); 
        answer.addAttribute(new Attribute("resultType", identityColumnType)); 
        answer.addAttribute(new Attribute(
                "keyProperty", introspectedColumn.getJavaProperty())); 
        answer.addAttribute(new Attribute("order", generatedKey.getMyBatis3Order())); 

        answer.addElement(new TextElement(generatedKey.getRuntimeSqlStatement()));

        return answer;
    }

    protected XmlElement getBaseColumnListElement() {
        XmlElement answer = new XmlElement("include"); 
        answer.addAttribute(new Attribute("refid", introspectedTable.getBaseColumnListId())); 
        return answer;
    }

    protected XmlElement getBlobColumnListElement() {
        XmlElement answer = new XmlElement("include"); 
        answer.addAttribute(new Attribute("refid", introspectedTable.getBlobColumnListId())); 
        return answer;
    }

    protected XmlElement getExampleIncludeElement() {
        XmlElement ifElement = new XmlElement("if"); 
        ifElement.addAttribute(new Attribute("test", "_parameter != null"));

        XmlElement includeElement = new XmlElement("include"); 
        includeElement.addAttribute(new Attribute("refid", introspectedTable.getExampleWhereClauseId())); 
        ifElement.addElement(includeElement);

        return ifElement;
    }

    protected XmlElement getUpdateByExampleIncludeElement() {
        XmlElement ifElement = new XmlElement("if"); 
        ifElement.addAttribute(new Attribute("test", "example != null"));  

        XmlElement includeElement = new XmlElement("include"); 
        includeElement.addAttribute(new Attribute("refid", 
                introspectedTable.getMyBatis3UpdateByExampleWhereClauseId()));
        ifElement.addElement(includeElement);

        return ifElement;
    }

    protected List<TextElement> buildSelectList(List<IntrospectedColumn> columns) {
        return buildSelectList("", columns); 
    }

    protected List<TextElement> buildSelectList(String initial, List<IntrospectedColumn> columns) {
        List<TextElement> answer = new ArrayList<>();
        StringBuilder sb = new StringBuilder(initial);
        Iterator<IntrospectedColumn> iter = columns.iterator();
        while (iter.hasNext()) {
            sb.append(MyBatis3FormattingUtilities.getSelectListPhrase(iter.next()));

            if (iter.hasNext()) {
                sb.append(", "); 
            }

            if (sb.length() > 80) {
                answer.add(new TextElement(sb.toString()));
                sb.setLength(0);
            }
        }

        if (sb.length() > 0) {
            answer.add(new TextElement(sb.toString()));
        }

        return answer;
    }

    protected List<TextElement> buildPrimaryKeyWhereClause() {
        List<TextElement> answer = new ArrayList<>();
        boolean first = true;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            String line;
            if (first) {
                line = "where "; 
                first = false;
            } else {
                line = "  and "; 
            }

            line += MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            line += " = "; 
            line += MyBatis3FormattingUtilities.getParameterClause(introspectedColumn);
            answer.add(new TextElement(line));
        }

        return answer;
    }

    protected XmlElement buildInitialInsert(String statementId, FullyQualifiedJavaType parameterType) {
        XmlElement answer = new XmlElement("insert"); 

        answer.addAttribute(new Attribute("id", statementId)); 

        answer.addAttribute(new Attribute("parameterType", parameterType.getFullyQualifiedName())); 

        context.getCommentGenerator().addComment(answer);

        introspectedTable.getGeneratedKey().ifPresent(gk ->
                introspectedTable.getColumn(gk.getColumn()).ifPresent(introspectedColumn -> {
                    // if the column is null, then it's a configuration error. The
                    // warning has already been reported
                    if (gk.isJdbcStandard()) {
                        answer.addAttribute(new Attribute("useGeneratedKeys", "true"));  
                        answer.addAttribute(
                                new Attribute("keyProperty", introspectedColumn.getJavaProperty())); 
                        answer.addAttribute(
                                new Attribute("keyColumn", introspectedColumn.getActualColumnName())); 
                    } else {
                        answer.addElement(getSelectKey(introspectedColumn, gk));
                    }
                })
        );

        return answer;
    }

    protected enum ResultElementType {
        ID("id"), 
        RESULT("result"); 

        private final String value;

        ResultElementType(String value) {
            this.value = value;
        }
    }

    protected List<XmlElement> buildResultMapItems(ResultElementType elementType, List<IntrospectedColumn> columns) {
        List<XmlElement> answer = new ArrayList<>();
        for (IntrospectedColumn introspectedColumn : columns) {
            XmlElement resultElement = new XmlElement(elementType.value);

            resultElement.addAttribute(buildColumnAttribute(introspectedColumn));
            resultElement.addAttribute(new Attribute("property", introspectedColumn.getJavaProperty())); 
            resultElement.addAttribute(new Attribute("jdbcType", introspectedColumn.getJdbcTypeName())); 

            if (stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(
                        new Attribute("typeHandler", introspectedColumn.getTypeHandler())); 
            }

            answer.add(resultElement);
        }

        return answer;
    }

    protected XmlElement buildConstructorElement(boolean includeBlobColumns) {
        XmlElement constructor = new XmlElement("constructor"); 

        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            XmlElement resultElement = new XmlElement("idArg"); 

            resultElement.addAttribute(buildColumnAttribute(introspectedColumn));
            resultElement.addAttribute(new Attribute("jdbcType", 
                    introspectedColumn.getJdbcTypeName()));
            resultElement.addAttribute(new Attribute("javaType", 
                    introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));

            if (stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(
                        new Attribute("typeHandler", introspectedColumn.getTypeHandler())); 
            }

            constructor.addElement(resultElement);
        }

        List<IntrospectedColumn> columns;
        if (includeBlobColumns) {
            columns = introspectedTable.getNonPrimaryKeyColumns();
        } else {
            columns = introspectedTable.getBaseColumns();
        }
        for (IntrospectedColumn introspectedColumn : columns) {
            XmlElement resultElement = new XmlElement("arg"); 

            resultElement.addAttribute(buildColumnAttribute(introspectedColumn));
            resultElement.addAttribute(new Attribute("jdbcType", 
                    introspectedColumn.getJdbcTypeName()));

            if (introspectedColumn.getFullyQualifiedJavaType().isPrimitive()) {
                // need to use the MyBatis type alias for a primitive byte
                String s = '_'
                        + introspectedColumn.getFullyQualifiedJavaType().getShortName();
                resultElement.addAttribute(new Attribute("javaType", s)); 
            } else if ("byte[]".equals(introspectedColumn.getFullyQualifiedJavaType() 
                    .getFullyQualifiedName())) {
                // need to use the MyBatis type alias for a primitive byte arry
                resultElement.addAttribute(new Attribute("javaType", 
                        "_byte[]")); 
            } else {
                resultElement.addAttribute(new Attribute("javaType", 
                        introspectedColumn.getFullyQualifiedJavaType().getFullyQualifiedName()));
            }

            if (stringHasValue(introspectedColumn.getTypeHandler())) {
                resultElement.addAttribute(new Attribute(
                        "typeHandler", introspectedColumn.getTypeHandler())); 
            }

            constructor.addElement(resultElement);
        }

        return constructor;
    }

    protected Attribute buildColumnAttribute(IntrospectedColumn introspectedColumn) {
        return new Attribute("column", 
                MyBatis3FormattingUtilities.getRenamedColumnNameForResultMap(introspectedColumn));
    }

    protected XmlElement buildUpdateByExampleElement(String statementId, List<IntrospectedColumn> columns) {
        XmlElement answer = new XmlElement("update"); 

        answer.addAttribute(new Attribute("id", statementId)); 

        answer.addAttribute(new Attribute("parameterType", "map"));  
        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); 
        sb.append(introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); 

        Iterator<IntrospectedColumn> iter = ListUtilities.removeGeneratedAlwaysColumns(columns).iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            sb.append(MyBatis3FormattingUtilities.getAliasedEscapedColumnName(introspectedColumn));
            sb.append(" = "); 
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "row.")); 

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        answer.addElement(getUpdateByExampleIncludeElement());
        return answer;
    }

    protected XmlElement buildUpdateByPrimaryKeyElement(String statementId, String parameterType,
                                                        List<IntrospectedColumn> columns) {
        XmlElement answer = new XmlElement("update"); 

        answer.addAttribute(new Attribute("id", statementId)); 
        answer.addAttribute(new Attribute("parameterType", parameterType)); 

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();
        sb.append("update "); 
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new TextElement(sb.toString()));

        // set up for first column
        sb.setLength(0);
        sb.append("set "); 

        Iterator<IntrospectedColumn> iter = ListUtilities.removeGeneratedAlwaysColumns(columns).iterator();
        while (iter.hasNext()) {
            IntrospectedColumn introspectedColumn = iter.next();

            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = "); 
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn));

            if (iter.hasNext()) {
                sb.append(',');
            }

            answer.addElement(new TextElement(sb.toString()));

            // set up for the next column
            if (iter.hasNext()) {
                sb.setLength(0);
                OutputUtilities.xmlIndent(sb, 1);
            }
        }

        buildPrimaryKeyWhereClause().forEach(answer::addElement);

        return answer;
    }
}

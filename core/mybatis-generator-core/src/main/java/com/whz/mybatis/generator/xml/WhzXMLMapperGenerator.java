package com.whz.mybatis.generator.xml;

import com.whz.mybatis.generator.xml.resultmap.WhzResultMapElementGenerator;
import com.whz.mybatis.generator.xml.sql.*;
import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.XmlConstants;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.*;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * 
 * @author Jeff Butler
 * 
 */
public class WhzXMLMapperGenerator extends AbstractXmlGenerator {

    public WhzXMLMapperGenerator() {
        super();
    }

    @Override
    public Document getDocument() {
        Document document = new Document(
                XmlConstants.MYBATIS3_MAPPER_PUBLIC_ID,
                XmlConstants.MYBATIS3_MAPPER_SYSTEM_ID);
        document.setRootElement(getSqlMapElement());

        if (!context.getPlugins().sqlMapDocumentGenerated(document, introspectedTable)) {
            document = null;
        }

        return document;
    }

    protected XmlElement getSqlMapElement() {
        FullyQualifiedTable table = introspectedTable.getFullyQualifiedTable();
        progressCallback.startTask(getString("Progress.12", table.toString()));
        XmlElement answer = new XmlElement("mapper");
        String namespace = introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));

        context.getCommentGenerator().addRootComment(answer);

        // resultMap
        initializeAndExecuteGenerator(new WhzResultMapElementGenerator(true), answer);

        // baseColumn
        initializeAndExecuteGenerator(new WhzBaseColumnElementGenerator(), answer);

        // qc
        initializeAndExecuteGenerator(new WhzQcElementGenerator(), answer);

        // set
        initializeAndExecuteGenerator(new WhzSetElementGenerator(), answer);

        // batchSet
        initializeAndExecuteGenerator(new WhzBatchSetElementGenerator(), answer);

        // batchSetValue
        initializeAndExecuteGenerator(new WhzBatchInsertValueElementGenerator(), answer);


        // tableName
        initializeAndExecuteGenerator(new WhzTableNameElementGenerator(), answer);

        // page-limit
        initializeAndExecuteGenerator(new WhzPageLimitElementGenerator(), answer);

        // list
        initializeAndExecuteGenerator(new WhzListElementGenerator(), answer);

        // count
        initializeAndExecuteGenerator(new WhzCountElementGenerator(), answer);

        // insert
        initializeAndExecuteGenerator(new WhzInsertElementGenerator(), answer);

        // batchInsert
        initializeAndExecuteGenerator(new WhzBatchInsertElementGenerator(), answer);

        // update
        initializeAndExecuteGenerator(new WhzUpdateElementGenerator(), answer);

        // batchUpdate
        initializeAndExecuteGenerator(new WhzBatchUpdateElementGenerator(), answer);


        return answer;
    }

    protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator, XmlElement parentElement) {
        elementGenerator.setContext(context);
        elementGenerator.setIntrospectedTable(introspectedTable);
        elementGenerator.setProgressCallback(progressCallback);
        elementGenerator.setWarnings(warnings);
        elementGenerator.addElements(parentElement);
    }

}

package com.whz.mybatis.generator;

import com.whz.mybatis.generator.java.mapper.WhzJavaMapperGenerator;
import com.whz.mybatis.generator.java.model.WhzModelGenerator;
import com.whz.mybatis.generator.java.model.WhzModelQcGenerator;
import com.whz.mybatis.generator.xml.WhzXMLMapperGenerator;
import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.config.PropertyRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author 盖伦
 * @Date 2023/5/15
 */
public class WhzIntrospectedTableMyBatis3Impl extends IntrospectedTable {

    /** The java model generators. */
    protected List<AbstractJavaGenerator> javaModelGenerators;

    /** The client generators. */
    protected List<AbstractJavaGenerator> clientGenerators;

    /** The xml mapper generator. */
    protected AbstractXmlGenerator xmlMapperGenerator;

    public WhzIntrospectedTableMyBatis3Impl() {
        super(IntrospectedTable.TargetRuntime.MYBATIS3);
        javaModelGenerators = new ArrayList<AbstractJavaGenerator>();
        clientGenerators = new ArrayList<AbstractJavaGenerator>();
    }

    @Override
    public void calculateGenerators(List<String> warnings, ProgressCallback progressCallback) {
        // java model 的生成
        calculateJavaModelGenerators(warnings, progressCallback);

        // Dao层接口的生成
        AbstractJavaClientGenerator javaClientGenerator = calculateClientGenerators(warnings, progressCallback);

        // 使用自定义的XMLMapperGenerator来生成
        calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }

    /**
     * 创建JavaGenerator实现
     *
     * @param warnings
     * @param progressCallback
     */
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {

        AbstractJavaGenerator g1 = new WhzModelGenerator();
        initializeAbstractGenerator(g1, warnings, progressCallback);
        javaModelGenerators.add(g1);

        AbstractJavaGenerator g2 = new WhzModelQcGenerator();
        initializeAbstractGenerator(g2, warnings, progressCallback);
        javaModelGenerators.add(g2);

    }

    /**
     * Calculate client generators.
     *
     * @param warnings
     *            the warnings
     * @param progressCallback
     *            the progress callback
     * @return true if an XML generator is required
     */
    protected AbstractJavaClientGenerator calculateClientGenerators(List<String> warnings, ProgressCallback progressCallback) {

        AbstractJavaClientGenerator javaGenerator = createJavaClientGenerator();
        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        clientGenerators.add(javaGenerator);

        return javaGenerator;
    }
    /**
     * Creates the java client generator.
     *
     * @return the abstract java client generator
     */
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        return new WhzJavaMapperGenerator();
    }

    /**
     * 使用自定义的XMLMapperGenerator来生成
     *
     * @param javaClientGenerator
     *            the java client generator
     * @param warnings
     *            the warnings
     * @param progressCallback
     *            the progress callback
     */
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator, List<String> warnings, ProgressCallback progressCallback) {
        xmlMapperGenerator = new WhzXMLMapperGenerator();
        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
    }

    /**
     * Initialize abstract generator.
     *
     * @param abstractGenerator
     *            the abstract generator
     * @param warnings
     *            the warnings
     * @param progressCallback
     *            the progress callback
     */
    protected void initializeAbstractGenerator(AbstractGenerator abstractGenerator, List<String> warnings, ProgressCallback progressCallback) {
        if (abstractGenerator == null) {
            return;
        }

        abstractGenerator.setContext(context);
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }

    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> answer = new ArrayList<GeneratedJavaFile>();

        for (AbstractJavaGenerator javaGenerator : javaModelGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(
                        compilationUnit,
                        context.getJavaModelGeneratorConfiguration().getTargetProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        context.getJavaFormatter()
                );
                answer.add(gjf);
            }
        }

        for (AbstractJavaGenerator javaGenerator : clientGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(
                        compilationUnit,
                        context.getJavaClientGeneratorConfiguration().getTargetProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        context.getJavaFormatter()
                );
                answer.add(gjf);
            }
        }

        return answer;
    }

    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<GeneratedXmlFile>();

        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument();
            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                    getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(),
                    true, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }

        return answer;
    }

    @Override
    public int getGenerationSteps() {
        return javaModelGenerators.size() + clientGenerators.size() +
                (xmlMapperGenerator == null ? 0 : 1);
    }

    @Override
    public boolean isJava5Targeted() {
        return true;
    }

    @Override
    public boolean requiresXMLGenerator() {
        AbstractJavaClientGenerator javaClientGenerator = createJavaClientGenerator();

        if (javaClientGenerator == null) {
            return false;
        } else {
            return javaClientGenerator.requiresXMLGenerator();
        }
    }

}

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
package org.mybatis.generator.api;

import org.mybatis.generator.codegen.RootClassInfo;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.ShellException;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.XmlFileMergerJaxp;

import java.io.*;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mybatis.generator.internal.util.ClassloaderUtility.getCustomClassloader;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * This class is the main interface to MyBatis generator. A typical execution of the tool involves these steps:
 *
 * <ol>
 * <li>Create a Configuration object. The Configuration can be the result of a parsing the XML configuration file, or it
 * can be created solely in Java.</li>
 * <li>Create a MyBatisGenerator object</li>
 * <li>Call one of the generate() methods</li>
 * </ol>
 *
 * @author Jeff Butler
 * @see org.mybatis.generator.config.xml.ConfigurationParser
 */
public class MyBatisGenerator {

    private static final ProgressCallback NULL_PROGRESS_CALLBACK = new ProgressCallback() {};

    /** 配置类 */
    private final Configuration configuration;

    private final ShellCallback shellCallback;

    private final List<GeneratedJavaFile> generatedJavaFiles = new ArrayList<>();
    private final List<GeneratedXmlFile> generatedXmlFiles = new ArrayList<>();
    private final List<GeneratedKotlinFile> generatedKotlinFiles = new ArrayList<>();
    /** 由插件方法contextGenerateAdditionalFiles生成的任何类型的生成文件 */
    private final List<GeneratedFile> otherGeneratedFiles = new ArrayList<>();

    /** 用于保存运行过程中的告警信息 */
    private final List<String> warnings;

    private final Set<String> projects = new HashSet<>();




    /**
     * Constructs a MyBatisGenerator object.
     *
     * @param configuration
     *            The configuration for this invocation
     * @param shellCallback
     *            an instance of a ShellCallback interface. You may specify
     *            <code>null</code> in which case the DefaultShellCallback will
     *            be used.
     * @param warnings
     *            Any warnings generated during execution will be added to this
     *            list. Warnings do not affect the running of the tool, but they
     *            may affect the results. A typical warning is an unsupported
     *            data type. In that case, the column will be ignored and
     *            generation will continue. You may specify <code>null</code> if
     *            you do not want warnings returned.
     * @throws InvalidConfigurationException
     *             if the specified configuration is invalid
     */
    public MyBatisGenerator(Configuration configuration, ShellCallback shellCallback, List<String> warnings) throws InvalidConfigurationException {
        super();
        if (configuration == null) {
            throw new IllegalArgumentException(getString("RuntimeError.2")); 
        } else {
            this.configuration = configuration;
        }

        if (shellCallback == null) {
            this.shellCallback = new DefaultShellCallback(false);
        } else {
            this.shellCallback = shellCallback;
        }

        if (warnings == null) {
            this.warnings = new ArrayList<>();
        } else {
            this.warnings = warnings;
        }

        this.configuration.validate();
    }

    /**
     * 这是生成代码的主要方法。该方法运行时间较长，但可以提供进度，也可以通过 ProgressCallback 接口取消该方法。
     *
     * @param callback
     *            an instance of the ProgressCallback interface, or <code>null</code> if you do not require progress
     *            information
     * @param contextIds
     *            a set of Strings containing context ids to run. Only the contexts with an id specified in this list
     *            will be run. If the list is null or empty, than all contexts are run.
     * @param fullyQualifiedTableNames
     *            a set of table names to generate. The elements of the set must be Strings that exactly match what's
     *            specified in the configuration. For example, if table name = "foo" and schema = "bar", then the fully
     *            qualified table name is "foo.bar". If the Set is null or empty, then all tables in the configuration
     *            will be used for code generation.
     * @param writeFiles
     *            if true, then the generated files will be written to disk.  If false,
     *            then the generator runs but nothing is written
     * @throws SQLException
     *             the SQL exception
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     * @throws InterruptedException
     *             if the method is canceled through the ProgressCallback
     */
    public void generate(ProgressCallback callback, Set<String> contextIds, Set<String> fullyQualifiedTableNames, boolean writeFiles) throws SQLException, IOException, InterruptedException {

        if (callback == null) {
            callback = NULL_PROGRESS_CALLBACK;
        }

        // 一、初始化
        generatedJavaFiles.clear();
        generatedXmlFiles.clear();
        ObjectFactory.reset();
        RootClassInfo.reset();

        // 计算要运行的上下文
        List<Context> contextsToRun;
        if (contextIds == null || contextIds.isEmpty()) {
            contextsToRun = configuration.getContexts();
        } else {
            contextsToRun = new ArrayList<>();
            for (Context context : configuration.getContexts()) {
                if (contextIds.contains(context.getId())) {
                    contextsToRun.add(context);
                }
            }
        }

        // 如果需要，设置自定义类加载器
        if (!configuration.getClassPathEntries().isEmpty()) {
            ClassLoader classLoader = getCustomClassloader(configuration.getClassPathEntries());
            ObjectFactory.addExternalClassLoader(classLoader);
        }

        // 现在运行introspections...
        int totalSteps = 0;
        for (Context context : contextsToRun) {
            totalSteps += context.getIntrospectionSteps();
        }
        // 扩展方法
        callback.introspectionStarted(totalSteps);
        for (Context context : contextsToRun) {
            context.introspectTables(callback, warnings, fullyQualifiedTableNames);
        }

        // 现在运行生成文件
        totalSteps = 0;
        for (Context context : contextsToRun) {
            totalSteps += context.getGenerationSteps();
        }
        // 扩展方法
        callback.generationStarted(totalSteps);

        // 配置文件生成器
        for (Context context : contextsToRun) {
            context.generateFiles(callback, generatedJavaFiles, generatedXmlFiles, generatedKotlinFiles, otherGeneratedFiles, warnings);
        }



        // 生成文件，并写入代码
        if (writeFiles) {
            // 开始生成文件前，调用一个扩展方法
            callback.saveStarted(generatedXmlFiles.size() + generatedJavaFiles.size());

            // 生成xml文件
            for (GeneratedXmlFile gxf : generatedXmlFiles) {
                projects.add(gxf.getTargetProject());
                writeGeneratedXmlFile(gxf, callback);
            }

            // 生成java文件
            for (GeneratedJavaFile gjf : generatedJavaFiles) {
                projects.add(gjf.getTargetProject());
                writeGeneratedJavaFile(gjf, callback);
            }

            // 生成Kotlin文件
            for (GeneratedKotlinFile gkf : generatedKotlinFiles) {
                projects.add(gkf.getTargetProject());
                writeGeneratedFile(gkf, callback);
            }

            for (GeneratedFile gf : otherGeneratedFiles) {
                projects.add(gf.getTargetProject());
                writeGeneratedFile(gf, callback);
            }

            for (String project : projects) {
                shellCallback.refreshProject(project);
            }
        }

        // 文件写完后调用一个扩展方法
        callback.done();
    }
    public void generate(ProgressCallback callback) throws SQLException, IOException, InterruptedException {
        generate(callback, null, null, true);
    }
    public void generate(ProgressCallback callback, Set<String> contextIds) throws SQLException, IOException, InterruptedException {
        generate(callback, contextIds, null, true);
    }
    public void generate(ProgressCallback callback, Set<String> contextIds, Set<String> fullyQualifiedTableNames) throws SQLException, IOException, InterruptedException {
        generate(callback, contextIds, fullyQualifiedTableNames, true);
    }




    // 生成文件


    /**
     * 生成java文件
     *
     * @param gjf
     * @param callback
     * @throws InterruptedException
     * @throws IOException
     */
    private void writeGeneratedJavaFile(GeneratedJavaFile gjf, ProgressCallback callback) throws InterruptedException, IOException {
        File targetFile;
        String source;
        try {
            File directory = shellCallback.getDirectory(gjf.getTargetProject(), gjf.getTargetPackage());
            targetFile = new File(directory, gjf.getFileName());
            if (targetFile.exists()) {
                if (shellCallback.isMergeSupported()) {
                    source = shellCallback.mergeJavaFile(gjf.getFormattedContent(), targetFile, MergeConstants.getOldElementTags(), gjf.getFileEncoding());
                } else if (shellCallback.isOverwriteEnabled()) {
                    source = gjf.getFormattedContent();
                    warnings.add(getString("Warning.11", targetFile.getAbsolutePath()));
                } else {
                    source = gjf.getFormattedContent();
                    targetFile = getUniqueFileName(directory, gjf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.getAbsolutePath()));
                }
            } else {
                source = gjf.getFormattedContent();
            }

            callback.checkCancel();
            callback.startTask(getString("Progress.15", targetFile.getName()));
            writeFile(targetFile, source, gjf.getFileEncoding());
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    /**
     * 生成Kotlin文件
     *
     * @param gf
     * @param callback
     * @throws InterruptedException
     * @throws IOException
     */
    private void writeGeneratedFile(GeneratedFile gf, ProgressCallback callback) throws InterruptedException, IOException {
        File targetFile;
        String source;
        try {
            File directory = shellCallback.getDirectory(gf
                    .getTargetProject(), gf.getTargetPackage());
            targetFile = new File(directory, gf.getFileName());
            if (targetFile.exists()) {
                if (shellCallback.isOverwriteEnabled()) {
                    source = gf.getFormattedContent();
                    warnings.add(getString("Warning.11",
                            targetFile.getAbsolutePath()));
                } else {
                    source = gf.getFormattedContent();
                    targetFile = getUniqueFileName(directory, gf
                            .getFileName());
                    warnings.add(getString(
                            "Warning.2", targetFile.getAbsolutePath()));
                }
            } else {
                source = gf.getFormattedContent();
            }

            callback.checkCancel();
            callback.startTask(getString(
                    "Progress.15", targetFile.getName()));
            writeFile(targetFile, source, gf.getFileEncoding());
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    /**
     * 生成xml文件
     *
     * @param gxf
     * @param callback
     * @throws InterruptedException
     * @throws IOException
     */
    private void writeGeneratedXmlFile(GeneratedXmlFile gxf, ProgressCallback callback) throws InterruptedException, IOException {
        File targetFile;
        String source;
        try {
            File directory = shellCallback.getDirectory(gxf.getTargetProject(), gxf.getTargetPackage());
            targetFile = new File(directory, gxf.getFileName());
            if (targetFile.exists()) {
                if (gxf.isMergeable()) {
                    source = XmlFileMergerJaxp.getMergedSource(gxf, targetFile);
                } else if (shellCallback.isOverwriteEnabled()) {
                    source = gxf.getFormattedContent();
                    warnings.add(getString("Warning.11", targetFile.getAbsolutePath()));
                } else {
                    source = gxf.getFormattedContent();
                    targetFile = getUniqueFileName(directory, gxf.getFileName());
                    warnings.add(getString("Warning.2", targetFile.getAbsolutePath()));
                }
            } else {
                source = gxf.getFormattedContent();
            }

            callback.checkCancel();
            callback.startTask(getString("Progress.15", targetFile.getName()));
            // 将文件内容source写入目标文件targetFile
            writeFile(targetFile, source, gxf.getFileEncoding());
        } catch (ShellException e) {
            warnings.add(e.getMessage());
        }
    }

    /**
     * 核心写文件的方法
     *
     * @param file
     *            the file
     * @param content
     *            the content
     * @param fileEncoding
     *            the file encoding
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    private void writeFile(File file, String content, String fileEncoding) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file, false)) {
            OutputStreamWriter osw;
            if (fileEncoding == null) {
                osw = new OutputStreamWriter(fos);
            } else {
                osw = new OutputStreamWriter(fos, Charset.forName(fileEncoding));
            }

            try (BufferedWriter bw = new BufferedWriter(osw)) {
                bw.write(content);
            }
        }
    }

    /**
     * 用于生成文件名
     *
     * @param directory
     *            the directory
     * @param fileName
     *            the file name
     * @return the unique file name
     */
    private File getUniqueFileName(File directory, String fileName) {
        File answer = null;

        // try up to 1000 times to generate a unique file name
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 1000; i++) {
            sb.setLength(0);
            sb.append(fileName);
            sb.append('.');
            sb.append(i);

            File testFile = new File(directory, sb.toString());
            if (!testFile.exists()) {
                answer = testFile;
                break;
            }
        }

        if (answer == null) {
            throw new RuntimeException(getString(
                    "RuntimeError.3", directory.getAbsolutePath()));
        }

        return answer;
    }


    // getter ...

    /**
     * Returns the list of generated Java files after a call to one of the generate methods.
     * This is useful if you prefer to process the generated files yourself and do not want
     * the generator to write them to disk.
     *
     * @return the list of generated Java files
     */
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        return generatedJavaFiles;
    }

    /**
     * Returns the list of generated Kotlin files after a call to one of the generate methods.
     * This is useful if you prefer to process the generated files yourself and do not want
     * the generator to write them to disk.
     *
     * @return the list of generated Kotlin files
     */
    public List<GeneratedKotlinFile> getGeneratedKotlinFiles() {
        return generatedKotlinFiles;
    }

    /**
     * Returns the list of generated XML files after a call to one of the generate methods.
     * This is useful if you prefer to process the generated files yourself and do not want
     * the generator to write them to disk.
     *
     * @return the list of generated XML files
     */
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        return generatedXmlFiles;
    }
}

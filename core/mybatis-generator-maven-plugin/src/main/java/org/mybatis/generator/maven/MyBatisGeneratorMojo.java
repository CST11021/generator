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
package org.mybatis.generator.maven;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.ClassloaderUtility;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;
import org.mybatis.generator.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

/**
 * Goal which generates MyBatis/iBATIS artifacts.
 */
@Mojo(name = "generate",defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class MyBatisGeneratorMojo extends AbstractMojo {

    /*
            <!-- mvn mybatis-generator:generate -->
            <plugin>
                <groupId>org.mybatis.generator</groupId>
                <artifactId>mybatis-generator-maven-plugin</artifactId>
                <version>1.3.2</version>
                <configuration>
                    <verbose>true</verbose>
                    <overwrite>true</overwrite>
                    <configurationFile>${basedir}/src/main/resources/generatorConfig.xml</configurationFile>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>cn.gov.zcy.mybatis</groupId>
                        <artifactId>mybatis-generator-core</artifactId>
                        <version>2.0.0-SNAPSHOT</version>
                    </dependency>
                    <!-- 数据库驱动 -->
                    <dependency>
                        <groupId>mysql</groupId>
                        <artifactId>mysql-connector-java</artifactId>
                        <version>${mysql-connector-java.version}</version>
                    </dependency>
                </dependencies>
            </plugin>
     */

    /**
     * Maven Project.
     *
     */
    @Parameter(property="project",required=true,readonly=true)
    private MavenProject project;

    /** Output Directory. */
    @Parameter(property="mybatis.generator.outputDirectory", defaultValue="${project.build.directory}/generated-sources/mybatis-generator", required=true)
    private File outputDirectory;

    /** 代码生成插件的配置文件地址 */
    @Parameter(property="mybatis.generator.configurationFile",defaultValue="${project.basedir}/src/main/resources/generatorConfig.xml", required=true)
    private File configurationFile;

    /** 指定mojo是否将进度消息写入日志 */
    @Parameter(property="mybatis.generator.verbose", defaultValue="false")
    private boolean verbose;
    /** 如果文件已存在是否覆盖 */
    @Parameter(property="mybatis.generator.overwrite", defaultValue="false")
    private boolean overwrite;

    /** 生成代码之前要运行的SQL脚本文件的位置。如果为空, 则不会运行任何脚本。如果不为空，则jdbcDriver, jdbcURL必须提供，jdbcUserId和jdbcPassword也可以提供。*/
    @Parameter(property="mybatis.generator.sqlScript")
    private String sqlScript;
    /**
     * JDBC Driver to use if a sql.script.file is specified.
     */
    @Parameter(property="mybatis.generator.jdbcDriver")
    private String jdbcDriver;
    /**
     * JDBC URL to use if a sql.script.file is specified.
     */
    @Parameter(property="mybatis.generator.jdbcURL")
    private String jdbcURL;
    /**
     * JDBC user ID to use if a sql.script.file is specified.
     */
    @Parameter(property="mybatis.generator.jdbcUserId")
    private String jdbcUserId;
    /**
     * JDBC password to use if a sql.script.file is specified.
     */
    @Parameter(property="mybatis.generator.jdbcPassword")
    private String jdbcPassword;

    /** 设置给哪些表生成代码 */
    @Parameter(property="mybatis.generator.tableNames")
    private String tableNames;

    /**
     * Comma delimited list of contexts to generate.
     */
    @Parameter(property="mybatis.generator.contexts")
    private String contexts;

    /** 是否跳过生成 */
    @Parameter(property="mybatis.generator.skip", defaultValue="false")
    private boolean skip;

    public void execute() throws MojoExecutionException {
        if (skip) {
            getLog().info( "MyBatis generator is skipped." );
            return;
        }

    	LogFactory.setLogFactory(new MavenLogFactory(this));

    	// add resource directories to the classpath.  This is required to support
        // use of a properties file in the build.  Typically, the properties file
        // is in the project's source tree, but the plugin classpath does not
        // include the project classpath.
        List<Resource> resources = project.getResources();
        List<String> resourceDirectories = new ArrayList<String>();
        for (Resource resource: resources) {
            resourceDirectories.add(resource.getDirectory());
        }
        ClassLoader cl = ClassloaderUtility.getCustomClassloader(resourceDirectories);
        ObjectFactory.addResourceClassLoader(cl);

        if (configurationFile == null) {
            throw new MojoExecutionException(Messages.getString("RuntimeError.0"));
        }

        if (!configurationFile.exists()) {
            throw new MojoExecutionException(Messages.getString("RuntimeError.1", configurationFile.toString()));
        }

        runScriptIfNecessary();

        Set<String> fullyqualifiedTables = new HashSet<String>();
        if (StringUtility.stringHasValue(tableNames)) {
            StringTokenizer st = new StringTokenizer(tableNames, ",");
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }

        Set<String> contextsToRun = new HashSet<String>();
        if (StringUtility.stringHasValue(contexts)) {
            StringTokenizer st = new StringTokenizer(contexts, ",");
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    contextsToRun.add(s);
                }
            }
        }

        doGenerate(contextsToRun, fullyqualifiedTables);

        if (project != null && outputDirectory != null && outputDirectory.exists()) {
            project.addCompileSourceRoot(outputDirectory.getAbsolutePath());

            Resource resource = new Resource();
            resource.setDirectory(outputDirectory.getAbsolutePath());
            resource.addInclude("**/*.xml");
            project.addResource(resource);
        }
    }

    private void doGenerate(Set<String> contextsToRun, Set<String> fullyqualifiedTables) throws MojoExecutionException {
        List<String> warnings = new ArrayList<String>();

        try {
            ConfigurationParser cp = new ConfigurationParser(project.getProperties(), warnings);
            Configuration config = cp.parseConfiguration(configurationFile);

            ShellCallback callback = new MavenShellCallback(this, overwrite);

            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);

            myBatisGenerator.generate(new MavenProgressCallback(getLog(), verbose), contextsToRun, fullyqualifiedTables);

        } catch (XMLParserException e) {
            for (String error : e.getErrors()) {
                getLog().error(error);
            }

            throw new MojoExecutionException(e.getMessage());
        } catch (SQLException e) {
            throw new MojoExecutionException(e.getMessage());
        } catch (IOException e) {
            throw new MojoExecutionException(e.getMessage());
        } catch (InvalidConfigurationException e) {
            for (String error : e.getErrors()) {
                getLog().error(error);
            }

            throw new MojoExecutionException(e.getMessage());
        } catch (InterruptedException e) {
            // ignore (will never happen with the DefaultShellCallback)
        }

        for (String error : warnings) {
            getLog().warn(error);
        }
    }

    private void runScriptIfNecessary() throws MojoExecutionException {
        if (sqlScript == null) {
            return;
        }

        SqlScriptRunner scriptRunner = new SqlScriptRunner(sqlScript, jdbcDriver, jdbcURL, jdbcUserId, jdbcPassword);
        scriptRunner.setLog(getLog());
        scriptRunner.executeScript();
    }

    public File getOutputDirectory() {
        return outputDirectory;
    }
}

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

import org.mybatis.generator.exception.ShellException;

import java.io.File;

/**
 * This interface defines methods that a shell should support to enable
 * the generator
 * to work. A "shell" is defined as the execution environment (i.e. an
 * Eclipse plugin, and Ant task, a NetBeans plugin, etc.)
 *
 * <p>The default ShellCallback that is very low function and does
 * not support the merging of Java files. The default shell callback is
 * appropriate for use in well controlled environments where no changes
 * made to generated Java files.
 *
 * @author Jeff Butler
 */
public interface ShellCallback {

    /**
     * 根据targetProject + targetPackage 返回生成文件的保存目录
     *
     * @param targetProject
     *            the target project
     * @param targetPackage
     *            the target package
     * @return the directory (must exist)
     * @throws ShellException
     *             if the project/package cannot be resolved into a directory on the file system. In this case, the
     *             generator will not save the file it is currently working on. The generator will add the exception
     *             message to the list of warnings automatically.
     */
    File getDirectory(String targetProject, String targetPackage) throws ShellException;

    /**
     * This method is called if a newly generated Java file would
     * overwrite an existing file. This method should return the merged source
     * (formatted). The generator will write the merged source as-is to the file
     * system.
     *
     * <p>A merge typically follows these steps:
     * <ol>
     * <li>Delete any methods/fields in the existing file that have the
     * specified JavaDoc tag</li>
     * <li>Add any new super interfaces from the new file into the existing file
     * </li>
     * <li>Make sure that the existing file's super class matches the new file</li>
     * <li>Make sure that the existing file is of the same type as the existing
     * file (either interface or class)</li>
     * <li>Add any new imports from the new file into the existing file</li>
     * <li>Add all methods and fields from the new file into the existing file</li>
     * <li>Format the resulting source string</li>
     * </ol>
     *
     * <p>This method is called only if you return <code>true</code> from
     * <code>isMergeSupported()</code>.
     *
     * @param newFileSource
     *            the source of the newly generated Java file
     * @param existingFile
     *            the existing Java file
     * @param javadocTags
     *            the JavaDoc tags that denotes which methods and fields in the
     *            old file to delete (if the Java element has any of these tags,
     *            the element is eligible for merge)
     * @param fileEncoding
     *            the file encoding for reading existing Java files.  Can be null,
     *            in which case the platform default encoding will be used.
     * @return the merged source, properly formatted. The source will be saved
     *         exactly as returned from this method.
     * @throws ShellException
     *             if the file cannot be merged for some reason. If this
     *             exception is thrown, nothing will be saved and the
     *             existing file will remain undisturbed. The generator will add the
     *             exception message to the list of warnings automatically.
     */
    default String mergeJavaFile(String newFileSource, File existingFile, String[] javadocTags, String fileEncoding) throws ShellException {
        throw new UnsupportedOperationException();
    }

    /**
     * 如果回调支持 Java 合并，则返回 true，否则返回 false。
     * mergeJavaFile()方法只有在该方法返回 true 时才会被调用。
     *
     * @return a boolean specifying whether Java merge is supported or not
     */
    default boolean isMergeSupported() {
        return false;
    }

    /**
     * 如果生成器应覆盖现有文件（如果存在），则返回 true。
     * 仅当 isMergeSupported() 返回 false 并且存在将被生成的文件覆盖的文件时，才会调用此方法。如果您返回 true，那么我们将记录一条警告，指出哪个文件被覆盖了。
     *
     * @return true if you want to overwrite existing files
     */
    boolean isOverwriteEnabled();

    /**
     * 在将所有文件保存到文件系统后，将针对受生成运行影响的每个唯一项目调用此方法一次。如果您的 ide 需要被告知文件系统对象已创建或更新，则此方法很有用。如果您在 ide 之外运行，则您的实现不需要在此方法中执行任何操作。
     *
     * @param project
     *            the project to be refreshed
     */
    default void refreshProject(String project) {}

}

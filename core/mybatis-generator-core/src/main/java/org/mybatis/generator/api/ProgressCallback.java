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

/**
 * This interface can be implemented to return progress information from the file generation process.
 *
 * <p>During the execution of code generation, there are three main operations:
 * database introspection, code generation based on the results of
 * introspection, and then merging/saving generated files. Methods
 * in this interface accordingly and in this order:
 * <ol>
 * <li>introspectionStarted(int)</li>
 * <li>(Repeatedly) startTask(String)</li>
 * <li>generationStarted(int)</li>
 * <li>(Repeatedly) startTask(String)</li>
 * <li>saveStarted(int)</li>
 * <li>(Repeatedly) startTask(String)</li>
 * <li>done()</li>
 * </ol>
 *
 * <p>Periodically, the <code>checkCancel()</code> method will be called to see if the
 * method should be canceled.
 *
 * <p>For planning purposes, the most common use case will have a ratio of 20%
 * introspection tasks, 40% generation tasks, and 40% save tasks.
 *
 * @author Jeff Butler
 */
public interface ProgressCallback {
    /**
     * Called to note the start of the introspection phase, and to note the
     * maximum number of startTask messages that will be sent for the
     * introspection phase.
     *
     * @param totalTasks
     *            the maximum number of times startTask will be called for the
     *            introspection phase.
     */
    default void introspectionStarted(int totalTasks) {}

    /**
     * 调用以记录生成阶段的开始，并记录将为生成阶段发送的 startTask 消息的最大数量。
     *
     * @param totalTasks
     *            the maximum number of times startTask will be called for the
     *            generation phase.
     */
    default void generationStarted(int totalTasks) {}

    /**
     * 调用以记录文件保存阶段的开始，并记录将为文件保存阶段发送的 startTask 消息的最大数量。
     *
     * @param totalTasks
     *            the maximum number of times startTask will be called for the
     *            file saving phase.
     */
    default void saveStarted(int totalTasks) {}

    /**
     * Called to denote the beginning of a save task.
     *
     * @param taskName
     *            a descriptive name of the current work step
     */
    default void startTask(String taskName) {}

    /**
     * This method is called when all generated files have been saved.
     */
    default void done() {}

    /**
     * The method is called periodically during a long-running method.
     * If the implementation throws <code>InterruptedException</code> then
     * the method will be canceled. Any files that have already been saved will
     * remain on the file system.
     *
     * @throws InterruptedException
     *             if the operation should be halted
     */
    default void checkCancel() throws InterruptedException {}
}

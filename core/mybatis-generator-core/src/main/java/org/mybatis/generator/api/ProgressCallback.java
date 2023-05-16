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
package org.mybatis.generator.api;

/**
 * This interface can be implemented to return progress information from the
 * file generation process.
 * 
 * During the execution of code generation, there are three main operations:
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
 * <p>
 * Periodically, the <code>checkCancel()</code> method will be called to see if the
 * method should be canceled.
 * <p>
 * For planning purposes, the most common use case will have a ratio of 20%
 * introspection tasks, 40% generation tasks, and 40% save tasks.
 * 
 * @author Jeff Butler
 */
public interface ProgressCallback {

    /**
     * 1 生成代码开始前的回调
     * 
     * @param totalTasks    本次要执行表数量
     */
    void introspectionStarted(int totalTasks);

    /**
     * 2 生成代码开始前的回调
     * 
     * @param totalTasks    本次要执行表数量
     */
    void generationStarted(int totalTasks);

    /**
     * 3 生成代码开始前的回调
     * 
     * @param totalTasks
     *            the maximum number of times startTask will be called for the
     *            file saving phase.
     */
    void saveStarted(int totalTasks);

    /**
     * 4 每个表执行前都会调用一次方法
     * 
     * @param taskName
     *            a descriptive name of the current work step
     */
    void startTask(String taskName);

    /**
     * 5 所有任务执行完成回调
     */
    void done();

    /**
     * 在长时间运行的方法中，周期性地调用该方法。如果实现抛出InterruptedException那么该方法将被取消。任何已经保存的文件都将 保留在文件系统中。
     * 
     * @throws InterruptedException if the operation should be halted
     */
    void checkCancel() throws InterruptedException;

}

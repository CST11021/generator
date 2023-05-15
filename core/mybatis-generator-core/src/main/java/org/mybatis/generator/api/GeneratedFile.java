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
 * 保存所有生成的文件共有的信息的抽象类
 *
 * @author Jeff Butler
 */
public abstract class GeneratedFile {

    protected final String targetProject;

    protected GeneratedFile(String targetProject) {
        this.targetProject = targetProject;
    }

    /**
     * 返回生成文件的全部内容。
     * 客户端可以简单地将此方法返回的值保存为文件内容。诸如 @see org.mybatis.generator.api.GeneratedJavaFile 之类的子类提供了对文件部分的更细粒度的访问，但在需要全部内容的情况下仍然实现此方法。
     *
     * @return Returns the content.
     */
    public abstract String getFormattedContent();

    /**
     * 返回生成的文件名
     *
     * @return Returns the file name.
     */
    public abstract String getFileName();

    /**
     * 返回文件所在的包路径
     *
     * @return Returns the target project.
     */
    public abstract String getTargetPackage();

    /**
     * 是否可合并
     *
     * @return true, if is mergeable
     */
    public abstract boolean isMergeable();

    /**
     * 文件的Encoding
     *
     * @return
     */
    public abstract String getFileEncoding();



    public String getTargetProject() {
        return targetProject;
    }

    @Override
    public String toString() {
        return getFileName();
    }
}

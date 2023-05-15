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
package org.mybatis.generator.internal;

import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.exception.ShellException;

import java.io.File;
import java.util.StringTokenizer;

import static org.mybatis.generator.internal.util.messages.Messages.getString;

public class DefaultShellCallback implements ShellCallback {

    private final boolean overwrite;

    public DefaultShellCallback(boolean overwrite) {
        super();
        this.overwrite = overwrite;
    }

    @Override
    public File getDirectory(String targetProject, String targetPackage) throws ShellException {
        // targetProject: 必须是已经存在的目录
        //
        // targetPackage is interpreted as a subdirectory, but in package
        // format (with dots instead of slashes). The subdirectory will be
        // created if it does not already exist

        File targetProjectDirectory = new File(targetProject);
        if (!targetProjectDirectory.isDirectory()) {
            throw new ShellException(getString("Warning.9", targetProject));
        }

        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(targetPackage, "."); 
        while (st.hasMoreTokens()) {
            sb.append(st.nextToken());
            sb.append(File.separatorChar);
        }

        File directory = new File(targetProjectDirectory, sb.toString());
        if (!directory.isDirectory()) {
            boolean rc = directory.mkdirs();
            if (!rc) {
                throw new ShellException(getString("Warning.10", directory.getAbsolutePath()));
            }
        }

        return directory;
    }

    @Override
    public boolean isOverwriteEnabled() {
        return overwrite;
    }
}

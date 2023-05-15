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
package org.mybatis.generator.api.dom.java.render;

import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mybatis.generator.api.dom.java.render.RenderingUtilities.*;

/**
 * 生成java类的代码
 */
public class TopLevelClassRenderer {

    public String render(TopLevelClass topLevelClass) {
        List<String> lines = new ArrayList<>();

        // 类最上面的注释
        lines.addAll(topLevelClass.getFileCommentLines());
        // 类的包名
        lines.addAll(renderPackage(topLevelClass));
        //
        lines.addAll(renderStaticImports(topLevelClass));
        // import相关代码
        lines.addAll(renderImports(topLevelClass));
        // 类代码
        lines.addAll(renderInnerClassNoIndent(topLevelClass, topLevelClass));

        return lines.stream().collect(Collectors.joining(System.getProperty("line.separator")));
    }
}

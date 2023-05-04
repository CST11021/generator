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
package org.mybatis.generator.api.dom.xml.render;

import org.mybatis.generator.api.dom.xml.DocType;
import org.mybatis.generator.api.dom.xml.Document;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DocumentRenderer {

    public String render(Document document) {
        return Stream.of(renderXmlHeader(),
                renderDocType(document),
                renderRootElement(document))
                .flatMap(Function.identity())
                .collect(Collectors.joining(System.getProperty("line.separator")));
    }

    /**
     * <?xml version="1.0" encoding="UTF-8"?>
     *
     * @return
     */
    private Stream<String> renderXmlHeader() {
        return Stream.of("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    }

    /**
     * <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
     *
     * @param document
     * @return
     */
    private Stream<String> renderDocType(Document document) {
        return Stream.of("<!DOCTYPE "
                + document.getRootElement().getName()
                + document.getDocType().map(this::renderDocType).orElse("")
                + ">");
    }

    /**
     * <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
     *
     * @param docType
     * @return
     */
    private String renderDocType(DocType docType) {
        return " " + docType.accept(new DocTypeRenderer());
    }

    /**
     * 从根节点开始渲染
     *
     * @param document
     * @return
     */
    private Stream<String> renderRootElement(Document document) {
        return document.getRootElement().accept(new ElementRenderer());
    }
}

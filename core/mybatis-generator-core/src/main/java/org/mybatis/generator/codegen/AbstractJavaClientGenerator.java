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
package org.mybatis.generator.codegen;

/**
 * This class exists to that Java client generators can specify whether
 * an XML generator is required to match the methods in the
 * Java client.  For example, a Java client built entirely with
 * annotations does not need matching XML.
 *
 * @author Jeff Butler
 *
 */
public abstract class AbstractJavaClientGenerator extends AbstractJavaGenerator {

    private final boolean requiresXMLGenerator;

    protected AbstractJavaClientGenerator(String project, boolean requiresXMLGenerator) {
        super(project);
        this.requiresXMLGenerator = requiresXMLGenerator;
    }

    /**
     * 如果需要匹配的 XML 生成器，则返回 true。
     *
     * @return 如果生成器需要匹配的 XML，则为真
     */
    public boolean requiresXMLGenerator() {
        return requiresXMLGenerator;
    }

    /**
     * 返回与此客户端生成器关联的 XML 生成器的实例。
     *
     * @return 匹配的 XML 生成器。如果此生成器不需要 XML，则可能返回 null
     */
    public abstract AbstractXmlGenerator getMatchedXMLGenerator();
}

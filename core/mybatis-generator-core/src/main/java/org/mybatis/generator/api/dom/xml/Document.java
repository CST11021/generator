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
package org.mybatis.generator.api.dom.xml;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * The Class Document.
 *
 * @author Jeff Butler
 */
public class Document {
    
    /** The public id. */
    private String publicId;
    /** The system id. */
    private String systemId;
    /** root标签 */
    private XmlElement rootElement;

    public Document(String publicId, String systemId) {
        super();
        this.publicId = publicId;
        this.systemId = systemId;
    }
    public Document() {
        super();
    }


    /**
     * <?xml version="1.0" encoding="UTF-8"?>
     * <!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
     * <mapper>
     *     ...
     * </mapper>
     *
     * @return the formatted content
     */
    public String getFormattedContent() {
        StringBuilder sb = new StringBuilder();

        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        if (publicId != null && systemId != null) {
            OutputUtilities.newLine(sb);
            sb.append("<!DOCTYPE ");
            sb.append(rootElement.getName());
            sb.append(" PUBLIC \"");
            sb.append(publicId);
            sb.append("\" \"");
            sb.append(systemId);
            sb.append("\">");
        }

        // 折行
        OutputUtilities.newLine(sb);
        // 获取<mapper>root标签内容
        sb.append(rootElement.getFormattedContent(0));

        return sb.toString();
    }


    // getter and setter ...

    /**
     * Gets the root element.
     *
     * @return Returns the rootElement.
     */
    public XmlElement getRootElement() {
        return rootElement;
    }

    /**
     * Sets the root element.
     *
     * @param rootElement
     *            The rootElement to set.
     */
    public void setRootElement(XmlElement rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * Gets the public id.
     *
     * @return Returns the publicId.
     */
    public String getPublicId() {
        return publicId;
    }

    /**
     * Gets the system id.
     *
     * @return Returns the systemId.
     */
    public String getSystemId() {
        return systemId;
    }
}

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
package org.mybatis.generator.api.dom.java;

import org.mybatis.generator.api.dom.OutputUtilities;

/**
 * @author Jeff Butler
 */
public class Field extends JavaElement {

    /** 字段对应的DB表字段员数据 */
    private FullyQualifiedJavaType type;
    /** 字段名 */
    private String name;
    /**
     * 字段的初始化数据，例如：实现了Serializable接口，需要生成如下代码，这里记录的是"1886107927368103115L"字符串
     * private static final long serialVersionUID = 1886107927368103115L;
     * */
    private String initializationString;

    /** 字段是否添加 transient 修饰 */
    private boolean isTransient;
    /** 字段是否添加 volatile 修饰 */
    private boolean isVolatile;

    public Field() {
        // use a default name to avoid NPE
        this("foo", FullyQualifiedJavaType.getIntInstance());
    }
    public Field(Field field) {
        super(field);
        this.type = field.type;
        this.name = field.name;
        this.initializationString = field.initializationString;
    }
    public Field(String name, FullyQualifiedJavaType type) {
        super();
        this.name = name;
        this.type = type;
    }

    /**
     * 生成代码
     *
     * @param indentLevel
     * @param compilationUnit
     * @return
     */
    public String getFormattedContent(int indentLevel, CompilationUnit compilationUnit) {
        StringBuilder sb = new StringBuilder();

        // 生成字段注释代码
        addFormattedJavadoc(sb, indentLevel);
        // 生成字段注解
        addFormattedAnnotations(sb, indentLevel);

        OutputUtilities.javaIndent(sb, indentLevel);
        sb.append(getVisibility().getValue());

        if (isStatic()) {
            sb.append("static ");
        }

        if (isFinal()) {
            sb.append("final ");
        }

        if (isTransient()) {
            sb.append("transient ");
        }

        if (isVolatile()) {
            sb.append("volatile ");
        }

        sb.append(JavaDomUtils.calculateTypeName(compilationUnit, type));

        sb.append(' ');
        sb.append(name);

        if (initializationString != null && initializationString.length() > 0) {
            sb.append(" = ");
            sb.append(initializationString);
        }

        sb.append(';');

        return sb.toString();
    }


    // getter and setter...

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public FullyQualifiedJavaType getType() {
        return type;
    }
    public void setType(FullyQualifiedJavaType type) {
        this.type = type;
    }
    public String getInitializationString() {
        return initializationString;
    }
    public void setInitializationString(String initializationString) {
        this.initializationString = initializationString;
    }
    public boolean isTransient() {
        return isTransient;
    }
    public void setTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }
    public boolean isVolatile() {
        return isVolatile;
    }
    public void setVolatile(boolean isVolatile) {
        this.isVolatile = isVolatile;
    }
}

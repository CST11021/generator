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
package org.mybatis.generator.internal.util;

import static org.mybatis.generator.internal.util.JavaBeansUtil.getGetterMethodName;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getSetterMethodName;
import static org.mybatis.generator.internal.util.JavaBeansUtil.getValidPropertyName;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * @author Jeff Butler
 *
 */
public class JavaBeansUtilTest {

    /**
     *
     */
    public JavaBeansUtilTest() {
        super();
    }

    @Test
    public void testGetValidPropertyName() {
        assertEquals("eMail", getValidPropertyName("eMail"));
        assertEquals("firstName", getValidPropertyName("firstName"));
        assertEquals("URL", getValidPropertyName("URL"));
        assertEquals("XAxis", getValidPropertyName("XAxis"));
        assertEquals("a", getValidPropertyName("a"));
        assertEquals("b", getValidPropertyName("B"));
        assertEquals("yaxis", getValidPropertyName("Yaxis"));
        assertEquals("i_PARAM_INT_1", getValidPropertyName("I_PARAM_INT_1"));
        assertEquals("_fred", getValidPropertyName("_fred"));
        assertEquals("accountType", getValidPropertyName("AccountType"));
    }

    @Test
    public void testGetGetterMethodName() {
        assertEquals("geteMail", getGetterMethodName("eMail", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getFirstName", getGetterMethodName("firstName", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getURL", getGetterMethodName("URL", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getXAxis", getGetterMethodName("XAxis", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getA", getGetterMethodName("a", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("isActive", getGetterMethodName("active", FullyQualifiedJavaType.getBooleanPrimitiveInstance()));
        assertEquals("getI_PARAM_INT_1", getGetterMethodName("i_PARAM_INT_1", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("get_fred", getGetterMethodName("_fred", FullyQualifiedJavaType.getStringInstance()));
        assertEquals("getAccountType", getGetterMethodName("AccountType", FullyQualifiedJavaType.getStringInstance()));
    }

    @Test
    public void testGetSetterMethodName() {
        assertEquals("seteMail", getSetterMethodName("eMail"));
        assertEquals("setFirstName", getSetterMethodName("firstName"));
        assertEquals("setURL", getSetterMethodName("URL"));
        assertEquals("setXAxis", getSetterMethodName("XAxis"));
        assertEquals("setA", getSetterMethodName("a"));
        assertEquals("setI_PARAM_INT_1", getSetterMethodName("i_PARAM_INT_1"));
        assertEquals("set_fred", getSetterMethodName("_fred"));
        assertEquals("setAccountType", getSetterMethodName("AccountType"));
    }
}

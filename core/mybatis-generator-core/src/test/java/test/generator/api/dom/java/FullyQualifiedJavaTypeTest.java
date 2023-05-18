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
package test.generator.api.dom.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Set;
import java.util.TreeSet;

import org.junit.Test;
import org.mybatis.generator.api.dom.OutputUtilities;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

/**
 * @author Jeff Butler
 *
 */
public class FullyQualifiedJavaTypeTest {

    @Test
    public void testJavaType() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.lang.String");
        assertFalse(fqjt.isExplicitlyImported());
        assertEquals("String", fqjt.getShortName());
        assertEquals("java.lang.String", fqjt.getFullyQualifiedName());
        assertEquals("java.lang", fqjt.getPackageName());
        assertEquals(0, fqjt.getImportList().size());
    }

    @Test
    public void testSimpleType() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("com.foo.Bar");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("Bar", fqjt.getShortName());
        assertEquals("com.foo.Bar", fqjt.getFullyQualifiedName());
        assertEquals("com.foo", fqjt.getPackageName());
        assertEquals(1, fqjt.getImportList().size());
        assertEquals("com.foo.Bar", fqjt.getImportList().get(0));
    }

    @Test
    public void testSimpleType2() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("com.foo.bar");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("bar", fqjt.getShortName());
        assertEquals("com.foo.bar", fqjt.getFullyQualifiedName());
        assertEquals("com.foo", fqjt.getPackageName());
        assertEquals(1, fqjt.getImportList().size());
        assertEquals("com.foo.bar", fqjt.getImportList().get(0));
    }
    
    @Test
    public void testSimpleType3() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("int");
        assertFalse(fqjt.isExplicitlyImported());
        assertEquals("int", fqjt.getShortName());
        assertEquals("int", fqjt.getFullyQualifiedName());
        assertEquals("", fqjt.getPackageName());
        assertEquals(0, fqjt.getImportList().size());
    }
    
    @Test
    public void testGenericType1() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.util.List<java.lang.String>");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("List<String>", fqjt.getShortName());
        assertEquals("java.util.List<java.lang.String>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(1, fqjt.getImportList().size());
        assertEquals("java.util.List", fqjt.getImportList().get(0));
        assertEquals("java.util.List", fqjt.getFullyQualifiedNameWithoutTypeParameters());
    }


    @Test
    public void testGenericType2() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.util.Map<java.lang.String, java.util.List<java.lang.String>>");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("Map<String, List<String>>", fqjt.getShortName());
        assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.String>>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(2, fqjt.getImportList().size());
        assertEquals("java.util.Map", fqjt.getFullyQualifiedNameWithoutTypeParameters());
    }


    @Test
    public void testGenericType3() {
        FullyQualifiedJavaType listOfStrings = new FullyQualifiedJavaType("java.util.List");
        listOfStrings.addTypeArgument(new FullyQualifiedJavaType("java.lang.String"));

        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.util.Map");
        fqjt.addTypeArgument(new FullyQualifiedJavaType("java.lang.String"));
        fqjt.addTypeArgument(listOfStrings);

        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("Map<String, List<String>>", fqjt.getShortName());
        assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.String>>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(2, fqjt.getImportList().size());
        assertEquals("java.util.Map", fqjt.getFullyQualifiedNameWithoutTypeParameters());
    }

    @Test
    public void testGenericType4() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.util.List<java.util.Map<java.lang.String, java.lang.Object>>");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("List<Map<String, Object>>", fqjt.getShortName());
        assertEquals("java.util.List<java.util.Map<java.lang.String, java.lang.Object>>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(2, fqjt.getImportList().size());
        assertEquals("java.util.List", fqjt.getFullyQualifiedNameWithoutTypeParameters());
    }

    @Test
    public void testWildcardType1() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.util.Map<java.lang.String, ? extends com.foo.Bar>");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("Map<String, ? extends Bar>", fqjt.getShortName());
        assertEquals("java.util.Map<java.lang.String, ? extends com.foo.Bar>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(2, fqjt.getImportList().size());
        assertEquals("java.util.Map", fqjt.getFullyQualifiedNameWithoutTypeParameters());
    }

    @Test
    public void testWildcardType2() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.util.Map<java.lang.String, ?>");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("Map<String, ?>", fqjt.getShortName());
        assertEquals("java.util.Map<java.lang.String, ?>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(1, fqjt.getImportList().size());
        assertEquals("java.util.Map", fqjt.getImportList().get(0));
        assertEquals("java.util.Map", fqjt.getFullyQualifiedNameWithoutTypeParameters());
    }

    @Test
    public void testWildcardType3() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.util.Map<? extends java.util.List<?>, ?>");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("Map<? extends List<?>, ?>", fqjt.getShortName());
        assertEquals("java.util.Map<? extends java.util.List<?>, ?>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(2, fqjt.getImportList().size());
        assertEquals("java.util.Map", fqjt.getFullyQualifiedNameWithoutTypeParameters());
    }

    @Test
    public void testWildcardType4() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.util.Map<?, ?>");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("Map<?, ?>", fqjt.getShortName());
        assertEquals("java.util.Map<?, ?>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(1, fqjt.getImportList().size());
        assertEquals("java.util.Map", fqjt.getImportList().get(0));
        assertEquals("java.util.Map", fqjt.getFullyQualifiedNameWithoutTypeParameters());
    }

    @Test
    public void testWildcardType5() {
        FullyQualifiedJavaType fqjt =
            new FullyQualifiedJavaType("java.util.List<? extends java.util.Map<? super java.lang.Object, ?>>");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("List<? extends Map<? super Object, ?>>", fqjt.getShortName());
        assertEquals("java.util.List<? extends java.util.Map<? super java.lang.Object, ?>>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(2, fqjt.getImportList().size());
        assertEquals("java.util.List", fqjt.getFullyQualifiedNameWithoutTypeParameters());
    }

    @Test
    public void testUppercasePackage1() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("org.foo.Bar.Inner");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("Inner", fqjt.getShortName());
        assertEquals("org.foo.Bar.Inner", fqjt.getFullyQualifiedName());
        assertEquals("org.foo.Bar", fqjt.getPackageName());
        assertEquals(1, fqjt.getImportList().size());
        assertEquals("org.foo.Bar.Inner", fqjt.getImportList().get(0));
    }

    @Test
    public void testUppercasePackage2() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("org.foo.Bar.Inner.Inner");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("Inner", fqjt.getShortName());
        assertEquals("org.foo.Bar.Inner.Inner", fqjt.getFullyQualifiedName());
        assertEquals("org.foo.Bar.Inner", fqjt.getPackageName());
        assertEquals(1, fqjt.getImportList().size());
        assertEquals("org.foo.Bar.Inner.Inner", fqjt.getImportList().get(0));
    }

    @Test
    public void testUppercasePackage3() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.List<org.foo.Bar.Inner>");
        assertTrue(fqjt.isExplicitlyImported());
        assertEquals("List<Inner>", fqjt.getShortName());
        assertEquals("java.util.List<org.foo.Bar.Inner>", fqjt.getFullyQualifiedName());
        assertEquals("java.util", fqjt.getPackageName());
        assertEquals(2, fqjt.getImportList().size());
        assertTrue(fqjt.getImportList().contains("java.util.List"));
        assertTrue(fqjt.getImportList().contains("org.foo.Bar.Inner"));
    }
    
    @Test
    public void testImportList() {
        Set<FullyQualifiedJavaType> types = new TreeSet<FullyQualifiedJavaType>();
        
        types.add(new FullyQualifiedJavaType("foo.bar.Example"));
        types.add(new FullyQualifiedJavaType("foo.bar.Example.Criteria"));
        types.add(new FullyQualifiedJavaType("foo.bar.Example.Criterion"));
        assertEquals(3, types.size());
        
        Set<String> imports = OutputUtilities.calculateImports(types);
        assertEquals(3, imports.size());
    }
    
    @Test
    public void testByteArray1() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("byte[]");
        assertFalse(fqjt.isPrimitive());
        assertTrue(fqjt.isArray());
    }

    @Test
    public void testByteArray2() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("byte[ ]");
        assertFalse(fqjt.isPrimitive());
        assertTrue(fqjt.isArray());
    }

    @Test
    public void testStringArray() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.lang.String[]");
        assertFalse(fqjt.isPrimitive());
        assertTrue(fqjt.isArray());
    }

    @Test
    public void testComplexArray() {
        FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("java.util.List<String>[]");
        assertFalse(fqjt.isPrimitive());
        assertTrue(fqjt.isArray());
    }
}

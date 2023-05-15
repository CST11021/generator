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
package org.mybatis.generator.config;

/**
 * This class holds constants for all properties recognized by the different
 * configuration elements. This helps document and maintain the different
 * properties, and helps to avoid spelling errors.
 *
 * @author Jeff Butler
 *
 */
public class PropertyRegistry {
    private PropertyRegistry() {}

    public static final String ANY_ENABLE_SUB_PACKAGES = "enableSubPackages";

    /**
     * recognized by table and java model generator.
     */
    public static final String ANY_ROOT_CLASS = "rootClass";
    public static final String ANY_IMMUTABLE = "immutable";
    public static final String ANY_CONSTRUCTOR_BASED = "constructorBased";

    /**
     * recognized by table and java client generator.
     */
    public static final String ANY_ROOT_INTERFACE = "rootInterface";

    // table 标签相关的属性

    /** XML映射文件中生成的ResultMap使用列索引定义而不是列名称 */
    public static final String TABLE_USE_COLUMN_INDEXES = "useColumnIndexes";
    /** 如果设置为true，生成的model类会直接使用column本身的名字，而不会再使用驼峰命名方法，比如BORN_DATE，生成的属性名字就是BORN_DATE,而不会是bornDate */
    public static final String TABLE_USE_ACTUAL_COLUMN_NAMES = "useActualColumnNames";
    /** 是否把列名和列备注拼接起来生成实体类属性名 */
    public static final String TABLE_USE_COMPOUND_PROPERTY_NAMES = "useCompoundPropertyNames";
    /** 默认为false，是否在运行时忽略别名 如果为true，则不会在生成表的时候把schema和catalog作为表的前缀 */
    public static final String TABLE_IGNORE_QUALIFIERS_AT_RUNTIME = "ignoreQualifiersAtRuntime";
    /** 如果设置了runtimeCatalog，那么在生成的SQL中，使用该指定的catalog，而不是table元素上的catalog */
    public static final String TABLE_RUNTIME_CATALOG = "runtimeCatalog";
    /** 如果设置了runtimeSchema，那么在生成的SQL中，使用该指定的schema，而不是table元素上的schema */
    public static final String TABLE_RUNTIME_SCHEMA = "runtimeSchema";
    /** 如果设置了runtimeTableName，那么在生成的SQL中，使用该指定的tablename，而不是table元素上的tablename */
    public static final String TABLE_RUNTIME_TABLE_NAME = "runtimeTableName";
    /** 指定是否只生成domain类，如果设置为true，只生成domain类，如果还配置了sqlMapGenerator，那么在mapper XML文件中，只生成resultMap元素 */
    public static final String TABLE_MODEL_ONLY = "modelOnly";
    /** 注意，该属性只针对MyBatis3Simple有用，如果选择的runtime是MyBatis3Simple，那么会生成一个SelectAll方法，
     如果指定了selectAllOrderByClause，那么会在该SQL中添加指定的这个order条件； */
    public static final String TABLE_SELECT_ALL_ORDER_BY_CLAUSE = "selectAllOrderByClause";

    public static final String TABLE_DYNAMIC_SQL_SUPPORT_CLASS_NAME = "dynamicSqlSupportClassName";
    public static final String TABLE_DYNAMIC_SQL_TABLE_OBJECT_NAME = "dynamicSqlTableObjectName";


    // context标签相关的属性

    public static final String CONTEXT_BEGINNING_DELIMITER = "beginningDelimiter";
    public static final String CONTEXT_ENDING_DELIMITER = "endingDelimiter";
    public static final String CONTEXT_AUTO_DELIMIT_KEYWORDS = "autoDelimitKeywords";
    public static final String CONTEXT_JAVA_FILE_ENCODING = "javaFileEncoding";
    public static final String CONTEXT_JAVA_FORMATTER = "javaFormatter";
    public static final String CONTEXT_XML_FORMATTER = "xmlFormatter";
    public static final String CONTEXT_KOTLIN_FORMATTER = "kotlinFormatter";
    public static final String CONTEXT_KOTLIN_FILE_ENCODING = "kotlinFileEncoding";

    public static final String CLIENT_DYNAMIC_SQL_SUPPORT_PACKAGE = "dynamicSqlSupportPackage";

    public static final String TYPE_RESOLVER_FORCE_BIG_DECIMALS = "forceBigDecimals";
    public static final String TYPE_RESOLVER_USE_JSR310_TYPES = "useJSR310Types";

    public static final String MODEL_GENERATOR_TRIM_STRINGS = "trimStrings";
    public static final String MODEL_GENERATOR_EXAMPLE_PACKAGE = "exampleTargetPackage";
    public static final String MODEL_GENERATOR_EXAMPLE_PROJECT = "exampleTargetProject";

    public static final String COMMENT_GENERATOR_SUPPRESS_DATE = "suppressDate";
    public static final String COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS = "suppressAllComments";
    public static final String COMMENT_GENERATOR_ADD_REMARK_COMMENTS = "addRemarkComments";
    public static final String COMMENT_GENERATOR_DATE_FORMAT = "dateFormat";
    public static final String COMMENT_GENERATOR_USE_LEGACY_GENERATED_ANNOTATION = "useLegacyGeneratedAnnotation";

    public static final String COLUMN_OVERRIDE_FORCE_JAVA_TYPE = "forceJavaTypeIntoMapping";
}

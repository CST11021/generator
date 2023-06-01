package com.whz.mybatis.generator.plugin;


import org.mybatis.generator.api.IntrospectedTable;

/**
 * This plugin demonstrates overriding the initialized() method to rename the
 * generated example classes. Instead of xxxExample, the classes will be named
 * xxxCriteria
 * 
 * This plugin accepts two properties:
 * <ul>
 * <li><tt>searchString</tt> (required) the regular expression of the name
 * search.</li>
 * <li><tt>replaceString</tt> (required) the replacement String.</li>
 * </ul>
 * 
 * For example, to change the name of the generated Example classes from
 * xxxExample to xxxCriteria, specify the following:
 * 
 * <dl>
 * <dt>searchString</dt>
 * <dd>Example$</dd>
 * <dt>replaceString</dt>
 * <dd>Criteria</dd>
 * </dl>
 * 
 * 
 * @author Jeff Butler
 * 
 */
public class WhzRenameSqlMapperPlugin extends WhzBaseRenamePlugin {

    public WhzRenameSqlMapperPlugin() {
	}

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.setMyBatis3XmlMapperFileName(getNameAfterReplace(introspectedTable.getMyBatis3XmlMapperFileName()));
    }

}


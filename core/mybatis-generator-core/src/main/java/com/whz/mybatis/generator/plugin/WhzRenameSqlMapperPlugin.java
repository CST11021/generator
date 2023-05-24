package com.whz.mybatis.generator.plugin;


import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

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
public class WhzRenameSqlMapperPlugin extends PluginAdapter {

    private String searchString;
    private String replaceString;
    private Pattern pattern;


    public WhzRenameSqlMapperPlugin() {
	}

	public boolean validate(List<String> warnings) {

        searchString = properties.getProperty("searchString");
        replaceString = properties.getProperty("replaceString");

        boolean valid = stringHasValue(searchString) && stringHasValue(replaceString);
        if (valid) {
            pattern = Pattern.compile(searchString);
        }

        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String oldType = introspectedTable.getMyBatis3XmlMapperFileName();
        Matcher matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);
        
        introspectedTable.setMyBatis3XmlMapperFileName(oldType);
    }
}


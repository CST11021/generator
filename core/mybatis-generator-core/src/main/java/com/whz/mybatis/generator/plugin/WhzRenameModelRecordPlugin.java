package com.whz.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @Author 盖伦
 * @Date 2023/5/24
 */
public class WhzRenameModelRecordPlugin extends PluginAdapter {

    private String postfixName;
    private String searchString;
    private String replaceString;
    private Pattern pattern;

    public WhzRenameModelRecordPlugin() {
    }

    /**
     * 是否使用该插件
     *
     * @param warnings
     *            add strings to this list to specify warnings. For example, if
     *            the plugin is invalid, you should specify why. Warnings are
     *            reported to users after the completion of the run.
     * @return
     */
    public boolean validate(List<String> warnings) {
        postfixName = properties.getProperty("postfixName");
        searchString = properties.getProperty("searchString");
        replaceString = properties.getProperty("replaceString");

        boolean valid = stringHasValue(searchString) && stringHasValue(replaceString);
        if (valid) {
            pattern = Pattern.compile(searchString);
        }

        return valid || stringHasValue(postfixName);
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String oldType = introspectedTable.getBaseRecordType();

        if (pattern != null) {
            Matcher matcher = pattern.matcher(oldType);
            oldType = matcher.replaceAll(replaceString);
        }


        if (stringHasValue(postfixName)) {
            oldType += postfixName;
        }
        introspectedTable.setBaseRecordType(oldType);
    }
}

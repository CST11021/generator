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
public class WhzRenameQueryRecordPlugin extends PluginAdapter {

    private String searchString;
    private String replaceString;
    private Pattern pattern;


    public WhzRenameQueryRecordPlugin() {
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
        String oldType = introspectedTable.getQueryRecordType();
        Matcher matcher = pattern.matcher(oldType);
        oldType = matcher.replaceAll(replaceString);

        introspectedTable.setQueryRecordType(oldType);
    }
}

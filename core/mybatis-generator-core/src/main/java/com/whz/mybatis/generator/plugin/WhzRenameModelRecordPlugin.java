package com.whz.mybatis.generator.plugin;

import org.apache.tools.ant.util.StringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @Author 盖伦
 * @Date 2023/5/24
 */
public class WhzRenameModelRecordPlugin extends PluginAdapter {

    private String postfixName;
    private String[] searchString;
    private String[] replaceString;

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
        String searchStringConfig = properties.getProperty("searchString");
        if (searchStringConfig != null) {
            searchString = searchStringConfig.split(",");
        }

        String replaceStringConfig = properties.getProperty("replaceString");
        if (replaceStringConfig != null) {
            replaceString = replaceStringConfig.split(",");
        }

        boolean valid = searchString != null && replaceString != null;
        return valid || stringHasValue(postfixName);
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String oldType = introspectedTable.getBaseRecordType();

        for (int i = 0; i < searchString.length; i++) {
            oldType.replace(searchString[i], replaceString[i]);
        }

        if (postfixName != null) {
            oldType += postfixName;
        }
        introspectedTable.setBaseRecordType(oldType);
    }
}

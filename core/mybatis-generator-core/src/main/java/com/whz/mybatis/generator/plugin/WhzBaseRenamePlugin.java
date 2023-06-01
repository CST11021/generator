package com.whz.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;

import java.util.Arrays;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;

/**
 * @Author 盖伦
 * @Date 2023/6/1
 */
public class WhzBaseRenamePlugin extends PluginAdapter {

    private String postfixName;
    private List<String> replaceString;

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

        String replaceStringConfig = properties.getProperty("replaceString");
        if (replaceStringConfig != null) {
            String[] ss = replaceStringConfig.split(",");
            if (ss != null) {
                replaceString = Arrays.asList(ss);
            }
        }

        return replaceString != null || stringHasValue(postfixName);
    }

    protected String getNameAfterReplace(String oldType) {

        for (String config : replaceString) {
            String[] rep = config.split(">");
            if (rep == null) {
                continue;
            }

            if (rep.length == 2) {
                oldType = oldType.replace(rep[0], rep[1]);
            } else {
                oldType = oldType.replace(rep[0], "");
            }

        }

        if (postfixName != null) {
            oldType += postfixName;
        }

        return oldType;
    }

}

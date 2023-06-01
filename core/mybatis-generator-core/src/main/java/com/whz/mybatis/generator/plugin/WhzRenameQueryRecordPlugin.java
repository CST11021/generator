package com.whz.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;

/**
 * @Author 盖伦
 * @Date 2023/5/24
 */
public class WhzRenameQueryRecordPlugin extends WhzBaseRenamePlugin {

    public WhzRenameQueryRecordPlugin() {
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.setQueryRecordType(getNameAfterReplace(introspectedTable.getQueryRecordType()));
    }
}

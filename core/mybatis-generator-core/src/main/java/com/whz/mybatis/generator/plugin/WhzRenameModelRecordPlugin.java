package com.whz.mybatis.generator.plugin;

import org.mybatis.generator.api.IntrospectedTable;

/**
 * @Author 盖伦
 * @Date 2023/5/24
 */
public class WhzRenameModelRecordPlugin extends WhzBaseRenamePlugin {

    public WhzRenameModelRecordPlugin() {
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.setBaseRecordType(getNameAfterReplace(introspectedTable.getBaseRecordType()));
    }
}

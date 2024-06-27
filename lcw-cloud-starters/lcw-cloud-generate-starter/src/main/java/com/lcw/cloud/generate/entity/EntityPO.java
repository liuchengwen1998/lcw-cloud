package com.lcw.cloud.generate.entity;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.io.File;
import java.util.regex.Matcher;

/**
 * Mybatis-Generator 生成Entity类
 */
public class EntityPO extends FileOutConfig {

    private PackageConfig packageConfig;

    public EntityPO(String templatePath) {
        super(templatePath);
    }

    public EntityPO(String templatePath, PackageConfig packageConfig) {
        super(templatePath);
        this.packageConfig = packageConfig;
        // /generators/entity.java.vm
    }

    @Override
    public String outputFile(TableInfo tableInfo) {

        return String.format(System.getProperty("user.dir") + "/build" + File.separator + packageConfig.getEntity().replaceAll("\\.", Matcher.quoteReplacement(File.separator)) + File.separator + tableInfo.getEntityName() + "PO" + ConstVal.JAVA_SUFFIX, tableInfo.getEntityName());
    }


}

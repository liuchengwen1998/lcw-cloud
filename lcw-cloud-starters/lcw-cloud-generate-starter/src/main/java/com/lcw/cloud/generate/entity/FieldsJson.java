package com.lcw.cloud.generate.entity;

import com.baomidou.mybatisplus.generator.config.ConstVal;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;

import java.io.File;

/**
 * Mybatis-Generator 生成Entity类
 */
public class FieldsJson extends FileOutConfig {

    private PackageConfig packageConfig;

    public FieldsJson(String templatePath) {
        super(templatePath);
    }

    public FieldsJson(String templatePath, PackageConfig packageConfig) {
        super(templatePath);
        this.packageConfig = packageConfig;
        // /generators/entity.java.vm
    }

    @Override
    public String outputFile(TableInfo tableInfo) {

        return String.format(System.getProperty("user.dir") + "/build/json/" + File.separator + tableInfo.getEntityName() + "Fields.json" + ConstVal.JAVA_SUFFIX, tableInfo.getEntityName());
    }


}

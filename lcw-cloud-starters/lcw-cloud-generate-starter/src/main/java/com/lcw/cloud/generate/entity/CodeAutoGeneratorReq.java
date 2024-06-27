package com.lcw.cloud.generate.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzhang
 * @since 2022/8/2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeAutoGeneratorReq {

    /**
     * 数据库地址
     */
    private String dataBaseUrl;

    /**
     * 数据库账号
     */
    private String dataBaseUsername = "root";

    /**
     * 数据库密码
     */
    private String dataBasePwd;

    /**
     * 数据库名称
     */
    private String dataBaseName;

    /**
     * 子系统名称
     */
    private String module = "";

    /**
     * 子模块
     */
    private String model;

    /**
     * 需要生成的表名
     */
    private String[] generateTable = {};

    /**
     * 是否需要生成service
     */
    private Boolean needGeneratorService;

    /**
     * api文件路径
     */
    private String apiFilePath;

    /**
     * 表前缀
     */
    private String tablePrefix;

    /**
     * 字段前缀
     */
    private String fieldPrefix;

}

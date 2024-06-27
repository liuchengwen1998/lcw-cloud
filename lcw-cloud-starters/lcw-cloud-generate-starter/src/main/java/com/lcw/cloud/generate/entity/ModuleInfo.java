package com.lcw.cloud.generate.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 模块信息
 *
 * @author yzhang
 * @since 1.0
 */
@Data
public class ModuleInfo {

    /**
     * 子系统编号
     */
    private String subSystemCode;
    /**
     * 模块生成
     */
    private String moduleName;

    /**
     * kye 为二级模块名称
     */
    private Map<String, List<ApiInfo>> apis = new HashMap();
}

package com.lcw.cloud.mybatis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 基础BasePO对象
 *
 * @author yzhang
 * @since 1.0
 */
@Data
public class BasePO implements Serializable {

    private static final long serialVersionUID = 7713347491652171076L;
    /**
     * 编号
     */
    private Long id;

    /**
     * 当前页码
     */
    private int pageNum = 1;

    /**
     * 每页显示条数
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 开始时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}

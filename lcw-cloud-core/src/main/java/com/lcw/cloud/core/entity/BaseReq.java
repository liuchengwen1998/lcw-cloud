package com.lcw.cloud.core.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yzhang
 * @desc Base REQ
 * @since 分页接口提供基础的查询参数
 */
@Data
public class BaseReq implements Serializable {

    private static final long serialVersionUID = 6156905077478024719L;

    /**
     * 当前页码
     */
    private Integer pageNum = 1;

    /**
     * 每页显示条数
     */
    private Integer pageSize = 10;

}

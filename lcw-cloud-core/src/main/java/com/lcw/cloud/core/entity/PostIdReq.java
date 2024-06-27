package com.lcw.cloud.core.entity;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yzhang
 * @date 2020/11/3
 * @desc 添加更新操作基类
 */
public class PostIdReq implements Serializable {

    private static final long serialVersionUID = 6156265077478024719L;

    @NotNull(message = "id不能为空")
    @Min(value = 1, message = "参数非法")
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}

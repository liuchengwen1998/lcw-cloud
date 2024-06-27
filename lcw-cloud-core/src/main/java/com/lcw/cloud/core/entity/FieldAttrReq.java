package com.lcw.cloud.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author yzhang
 * @since 2022/8/18
 * 动态属性字段
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FieldAttrReq implements Serializable {

    private static final long serialVersionUID = -1731873857548036524L;

    @NotBlank(message = "动态字段key不能为空")
    private String key;

    @NotNull(message = "值不能为空")
    private Object val;

}

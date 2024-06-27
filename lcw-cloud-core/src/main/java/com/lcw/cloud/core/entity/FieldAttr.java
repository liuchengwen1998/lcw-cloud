package com.lcw.cloud.core.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class FieldAttr implements Serializable {

    private static final long serialVersionUID = -1731873857548036524L;

    private String key;

    private Object val;

}

package com.lcw.cloud.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 验证失败的字段描述
 *
 * @author ：yzhang
 * @since ：2022/3/3 10:13
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FailureField implements Serializable {

    private static final long serialVersionUID = -3680656836001970583L;

    /**
     * 字段名
     */
    private String fieldName;

    /**
     * 提示信息
     */
    private String msg;

    public static FailureField of(String fieldName) {
        FailureField field = new FailureField();
        field.setFieldName(fieldName);
        return field;
    }
}

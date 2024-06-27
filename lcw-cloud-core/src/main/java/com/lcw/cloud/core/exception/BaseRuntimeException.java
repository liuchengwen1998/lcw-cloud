package com.lcw.cloud.core.exception;

import lombok.Data;

/**
 * 全局异常类
 *
 * @author ：yzhang
 * @since  ：2022/7/27 09:55
 */
@Data
public class BaseRuntimeException extends RuntimeException {

    /**
     * 错误码
     */
    protected String code;

    /**
     * 错误信息
     */
    protected String message;

    /**
     * 构造器
     *
     * @param code    错误码
     * @param message 错误信息
     */
    public BaseRuntimeException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

}

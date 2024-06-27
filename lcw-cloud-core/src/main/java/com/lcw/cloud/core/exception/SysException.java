package com.lcw.cloud.core.exception;

/**
 * 基础系统异常类，其它系统异常类必须继承此类
 *
 * @author yzhang
 * @since ：2022/7/27 09:55
 */
public class SysException extends BaseRuntimeException {

    public SysException(String code, String message) {
        super(code, message);
    }
}

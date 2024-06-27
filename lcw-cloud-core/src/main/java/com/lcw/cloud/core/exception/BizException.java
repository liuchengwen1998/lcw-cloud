package com.lcw.cloud.core.exception;

/**
 * 基础业务异常类，其它异常类必须集成此类
 *
 * @author ：yzhang
 * @since ：2022/7/27 09:55
 */
public class BizException extends BaseRuntimeException {

    public BizException(String code, String message) {
        super(code, message);
    }
}

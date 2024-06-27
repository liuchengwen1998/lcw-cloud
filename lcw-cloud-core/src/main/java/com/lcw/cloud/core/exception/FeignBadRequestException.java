package com.lcw.cloud.core.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;

/**
 * @author yzhang
 * @since 2022/8/9
 * feign自定义异常
 */
public class FeignBadRequestException extends HystrixBadRequestException {


    private String code;

    private String message;

    public FeignBadRequestException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

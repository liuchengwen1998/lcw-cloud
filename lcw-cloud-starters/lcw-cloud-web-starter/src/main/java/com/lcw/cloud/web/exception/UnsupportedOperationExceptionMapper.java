package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;

import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:34
 * 暂不支持该类型异常处理
 */
public class UnsupportedOperationExceptionMapper implements IExceptionMapper<Exception> {

    @Override
    public Result toResponse(Exception e, HttpServletResponse response) {
        response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_400.getCode()));
        UnsupportedOperationException exception = (UnsupportedOperationException) e;
        return Result.error(HttpStatus.HTTP_STATUS_400.getCode(), exception.getMessage());
    }

    @Override
    public Boolean support(Exception e) {
        return e instanceof UnsupportedOperationException;
    }
}

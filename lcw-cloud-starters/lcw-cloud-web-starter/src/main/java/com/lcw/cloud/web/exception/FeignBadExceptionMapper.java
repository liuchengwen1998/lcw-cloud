package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.exception.FeignBadRequestException;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;

import javax.servlet.http.HttpServletResponse;

/**
 * 默认的异常处理类，兜底处理类
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:34
 * feign获取调用方获取的异常
 */
public class FeignBadExceptionMapper extends AbstractExceptionMapper implements IExceptionMapper<Exception> {

    @Override
    public Result toResponse(Exception e, HttpServletResponse response) {
        response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_505.getCode()));
        FeignBadRequestException ex = (FeignBadRequestException) e;
        // 消息往外暴露
        noticeToOutContext.notice(HttpStatus.HTTP_STATUS_505.getCode(), e.getMessage());
        return Result.error(ex.getCode(), ex.getMessage());
    }

    @Override
    public Boolean support(Exception e) {
        return e instanceof FeignBadRequestException;
    }
}

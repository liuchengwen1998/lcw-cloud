package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.exception.BaseRuntimeException;
import com.lcw.cloud.core.exception.BizException;
import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;

import javax.servlet.http.HttpServletResponse;

/**
 * 框架自定义异常类,最常用，优先级最高
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:36
 */
public class BaseRuntimeExceptionMapper extends AbstractExceptionMapper implements IExceptionMapper<BaseRuntimeException> {



    @Override
    public Result toResponse(BaseRuntimeException e, HttpServletResponse response) {
        response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_505.getCode()));
        if (e instanceof BizException) {
            LogFactory.bizErr(e.getMessage());
        } else {
            LogFactory.err(e.getMessage(), e);
            // 消息往外暴露
            noticeToOutContext.notice(e.getCode(), e.getMessage());
        }
        return Result.error(e.getCode(), e.getMessage());
    }

    @Override
    public Boolean support(Exception e) {
        return e instanceof BaseRuntimeException;
    }
}

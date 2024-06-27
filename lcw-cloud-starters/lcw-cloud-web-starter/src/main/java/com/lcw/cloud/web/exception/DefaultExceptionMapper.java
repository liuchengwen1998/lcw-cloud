package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;

import javax.servlet.http.HttpServletResponse;

/**
 * 默认的异常处理类，兜底处理类
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:34
 */
public class DefaultExceptionMapper extends AbstractExceptionMapper implements IExceptionMapper<Exception> {

    @Override
    public Result toResponse(Exception e, HttpServletResponse response) {
        response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_500.getCode()));
        LogFactory.err(e.getMessage(), e);
        // 消息往外暴露
        noticeToOutContext.notice(HttpStatus.HTTP_STATUS_500.getCode(), e.getMessage());
        return HttpStatus.HTTP_STATUS_500.result();
    }

    @Override
    public Boolean support(Exception e) {
        return e != null;
    }
}

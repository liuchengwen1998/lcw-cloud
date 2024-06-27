package com.lcw.cloud.web.exception;

import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.rest.constant.RestErrorMessage;
import com.lcw.cloud.core.rest.entity.Result;
import org.springframework.web.HttpMediaTypeNotSupportedException;

import javax.servlet.http.HttpServletResponse;

import static com.lcw.cloud.core.rest.entity.HttpStatus.HTTP_STATUS_403;

/**
 * ContentType类型错误异常处理类
 *
 * @author ：yzhang
 * @since ：2022/3/3 14:15
 */
public class HttpMediaTypeNotSupportedExceptionMapper implements
        IExceptionMapper<HttpMediaTypeNotSupportedException> {

    @Override
    public Result toResponse(HttpMediaTypeNotSupportedException e, HttpServletResponse response) {
        response.setStatus(Integer.parseInt(HTTP_STATUS_403.getCode()));
        LogFactory.err(e.getMessage(), e);
        return Result.error(HTTP_STATUS_403.getCode(), RestErrorMessage.format(RestErrorMessage.MEDIA_TYPE_ERROR_MESSAGE, e.getContentType().getType()));
    }

    @Override
    public Boolean support(Exception e) {
        return e instanceof HttpMediaTypeNotSupportedException;
    }
}

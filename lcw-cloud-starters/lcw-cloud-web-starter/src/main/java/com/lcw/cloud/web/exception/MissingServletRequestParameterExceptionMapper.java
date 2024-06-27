package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.rest.constant.RestErrorMessage;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import org.springframework.web.bind.MissingServletRequestParameterException;

import javax.servlet.http.HttpServletResponse;

/**
 * MissingServletRequestParameterException异常处理类
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:09
 */
public class MissingServletRequestParameterExceptionMapper implements
        IExceptionMapper<MissingServletRequestParameterException> {

    @Override
    public Result toResponse(MissingServletRequestParameterException e, HttpServletResponse response) {
        response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_400.getCode()));
        return HttpStatus.HTTP_STATUS_400.result(
                RestErrorMessage.format(RestErrorMessage.REQUEST_PARAMETER_MISSING_ERROR_MESSAGE, e.getParameterName()));
    }
    @Override
    public Boolean support(Exception e) {
        return e instanceof MissingServletRequestParameterException;
    }
}

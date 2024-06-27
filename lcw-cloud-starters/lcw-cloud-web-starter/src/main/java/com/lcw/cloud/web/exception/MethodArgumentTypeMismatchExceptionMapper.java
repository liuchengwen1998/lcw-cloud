package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.rest.constant.RestErrorMessage;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletResponse;

/**
 * 参数类型不匹配异常处理类
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:05
 */
public class MethodArgumentTypeMismatchExceptionMapper extends AbstractExceptionMapper implements
        IExceptionMapper<MethodArgumentTypeMismatchException> {

    @Override
    public Result toResponse(MethodArgumentTypeMismatchException e, HttpServletResponse response) {
        response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_400.getCode()));
        // 消息往外暴露
        noticeToOutContext.notice(HttpStatus.HTTP_STATUS_400.getCode(), e.getMessage());
        return HttpStatus.HTTP_STATUS_400.result(
                RestErrorMessage.format(RestErrorMessage.ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE, e.getName()));
    }

    @Override
    public Boolean support(Exception e) {
        return e instanceof MethodArgumentTypeMismatchException;
    }
}

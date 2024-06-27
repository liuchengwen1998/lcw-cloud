package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.entity.FailureField;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import org.springframework.beans.TypeMismatchException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 默认的异常处理类，兜底处理类
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:34
 * 数据绑定异常处理
 */
public class BindExceptionMapper extends AbstractExceptionMapper implements IExceptionMapper<Exception> {

    @Override
    public Result toResponse(Exception e, HttpServletResponse response) {
        response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_400.getCode()));
        List<FailureField> list = new ArrayList<>();
        BindException exception = (BindException) e;
        Iterator<FieldError> iterator = exception.getBindingResult().getFieldErrors().iterator();
        while (iterator.hasNext()) {
            FieldError next = iterator.next();
            FailureField field = FailureField.of(next.getField());
            if (next.contains(TypeMismatchException.class)) {
                field.setMsg("数据类型错误");
            } else {
                field.setMsg(next.getDefaultMessage());
            }
            list.add(field);
        }
        // 消息往外暴露
        noticeToOutContext.notice(HttpStatus.HTTP_STATUS_400.getCode(), list.get(0).getMsg());
        return Result.error(HttpStatus.HTTP_STATUS_400.getCode(), list.get(0).getMsg(), list);
    }

    @Override
    public Boolean support(Exception e) {
        return e instanceof BindException;
    }
}

package com.lcw.cloud.web.exception;

import com.lcw.cloud.core.entity.FailureField;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MethodArgumentNotValidException处理类，为参数校验不通过异常
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:26
 */
public class MethodArgumentNotValidExceptionMapper extends AbstractExceptionMapper implements
        IExceptionMapper<MethodArgumentNotValidException> {

    @Override
    public Result toResponse(MethodArgumentNotValidException e, HttpServletResponse response) {
        response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_400.getCode()));
        List<FailureField> fields = e.getBindingResult().getFieldErrors().stream().map(error -> new FailureField(error.getField(), error.getDefaultMessage())).collect(
                Collectors.toList());
        // 消息往外暴露
        noticeToOutContext.notice(HttpStatus.HTTP_STATUS_400.getCode(), fields.get(0).getMsg());
        // 错误信息 默认提示第一个错误信息
        return Result.error(HttpStatus.HTTP_STATUS_400.getCode(), fields.get(0).getMsg(), fields);
    }

    @Override
    public Boolean support(Exception e) {
        return e instanceof MethodArgumentNotValidException;
    }
}

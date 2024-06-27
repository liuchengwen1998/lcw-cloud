package com.lcw.cloud.web.exception;

import com.lcw.cloud.core.rest.entity.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * WEB服务全局异常处理类,通过定义controller的advice实现, 只能捕获请求到达controller之后的异常.
 * 对异常进行处理, 处理顺序
 *
 * @author ：yzhang
 * @since ：2022/1/28 22:11
 * 1.0.0
 */
@RestControllerAdvice
@ConditionalOnMissingBean(GlobalRestExceptionResolver.class)
public class GlobalRestExceptionResolver {

    private final List<IExceptionMapper> exceptionMappers = new LinkedList<>();

    public GlobalRestExceptionResolver setMapper(IExceptionMapper mapper) {
        exceptionMappers.add(mapper);
        return this;
    }

    private IExceptionMapper loadMapper(Exception e) {
        Optional<IExceptionMapper> optional = exceptionMappers.stream().filter(mapper -> mapper.support(e)).findFirst();
        return optional.get();
    }

    /**
     * 处理 {@code BaseBizException}类型异常
     *
     * @param e 异常对象
     * @return 返回{@code Result} 标准对象
     * @see Result {@link Result}
     */
    @ExceptionHandler(Exception.class)
    public Result resolveBaseBizException(Exception e, HttpServletResponse response) {
        return loadMapper(e).toResponse(e, response);
    }

}

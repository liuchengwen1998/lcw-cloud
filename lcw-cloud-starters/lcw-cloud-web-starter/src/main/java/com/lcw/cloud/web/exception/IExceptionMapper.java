package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.rest.entity.Result;

import javax.servlet.http.HttpServletResponse;

/**
 * 自定义异常解析器接口类
 *
 * @author yzhang
 * @since 2022/2/10 10:30
 * @version 1.0.0
 */
public interface IExceptionMapper<T extends Exception> {

  Result<T> toResponse(T e, HttpServletResponse response);

  Boolean support(Exception e);

}

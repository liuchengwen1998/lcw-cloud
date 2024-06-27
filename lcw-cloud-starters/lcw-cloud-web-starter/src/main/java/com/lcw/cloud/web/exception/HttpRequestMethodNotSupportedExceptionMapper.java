package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.rest.constant.RestErrorMessage;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import javax.servlet.http.HttpServletResponse;

/**
 * Method方法不支持异常处理类
 *
 * @author ：yzhang
 * @since ：2022/3/3 14:26
 * 
 */
public class HttpRequestMethodNotSupportedExceptionMapper implements
    IExceptionMapper<HttpRequestMethodNotSupportedException> {

  @Override
  public Result toResponse(HttpRequestMethodNotSupportedException e, HttpServletResponse response) {
    response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_403.getCode()));
    LogFactory.err(HttpStatus.HTTP_STATUS_403.getMessage(), e);
    return HttpStatus.HTTP_STATUS_403.result(
        RestErrorMessage.format(RestErrorMessage.REQUEST_METHOD_NOT_SUPPORT_ERROR_MESSAGE, e.getMethod()));
  }

  @Override
  public Boolean support(Exception e) {
    return e instanceof HttpRequestMethodNotSupportedException;
  }
}

package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.rest.constant.RestErrorMessage;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import org.springframework.http.converter.HttpMessageNotReadableException;

import javax.servlet.http.HttpServletResponse;

/**
 * HttpMessageNotReadableException异常处理类，一般为包体无法读取
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:23
 * 
 */
public class HttpMessageNotReadableExceptionMapper extends AbstractExceptionMapper implements
    IExceptionMapper<HttpMessageNotReadableException> {

  @Override
  public Result<HttpMessageNotReadableException> toResponse(HttpMessageNotReadableException e, HttpServletResponse response) {
    response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_400.getCode()));
    LogFactory.bizErr(HttpStatus.HTTP_STATUS_400.getMessage(), e);
    // 消息往外暴露
    noticeToOutContext.notice(HttpStatus.HTTP_STATUS_400.getCode(), e.getMessage());
    return HttpStatus.HTTP_STATUS_400.result(RestErrorMessage.REQUEST_BODY_UNCORRECTED_ERROR_MESSAGE);
  }

  @Override
  public Boolean support(Exception e) {
    return e instanceof HttpMessageNotReadableException;
  }
}

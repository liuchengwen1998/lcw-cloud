package com.lcw.cloud.web.exception;


import com.lcw.cloud.core.rest.constant.RestErrorMessage;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import org.springframework.web.bind.MissingRequestHeaderException;

import javax.servlet.http.HttpServletResponse;

/**
 * Header参数不存在异常信息
 *
 * @author ：yzhang
 * @since ：2022/3/3 14:42
 * 
 */
public class MissingRequestHeaderExceptionMapper implements
    IExceptionMapper<MissingRequestHeaderException> {

  @Override
  public Result toResponse(MissingRequestHeaderException e, HttpServletResponse response) {
    response.setStatus(Integer.parseInt(HttpStatus.HTTP_STATUS_400.getCode()));
    return HttpStatus.HTTP_STATUS_400.result(
        RestErrorMessage.format(RestErrorMessage.REQUEST_HEADER_MISSING_ERROR_MESSAGE,e.getHeaderName()));
  }

  @Override
  public Boolean support(Exception e) {
    return e instanceof MissingRequestHeaderException;
  }
}

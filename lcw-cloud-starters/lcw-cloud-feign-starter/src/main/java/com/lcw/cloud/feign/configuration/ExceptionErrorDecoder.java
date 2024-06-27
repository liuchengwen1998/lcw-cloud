package com.lcw.cloud.feign.configuration;

import com.alibaba.fastjson.JSONObject;
import com.lcw.cloud.core.exception.FeignBadRequestException;
import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;

@Configuration
public class ExceptionErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {
        try {
            if (response.body() != null) {
                //会把异常信息转换成字符串，注意断点不要打在这一行，会报IO异常
                //断点可以打在它的下一行
                String body = Util.toString(response.body().asReader(Charset.defaultCharset()));
                //将字符串转换为自定义的异常信息
                LogFactory.bizError("调用内部系统出现错误，请求的服务为{},返回的错误信息为{}", s, body);
                Result result = JSONObject.parseObject(body, Result.class);
                //返回异常信息，随便返回哪个异常都行，主要是将code和message透传到前端
                return new FeignBadRequestException(result.getCode(), result.getMessage());
            }
        } catch (Exception ex) {
            //异常记录日志
            LogFactory.bizErr("Feign异常处理错误：", ex);
        }
        //默认返回"系统异常,请稍后重试"
        return new FeignBadRequestException(HttpStatus.HTTP_STATUS_500.getCode(), HttpStatus.HTTP_STATUS_500.getMessage());
    }

}

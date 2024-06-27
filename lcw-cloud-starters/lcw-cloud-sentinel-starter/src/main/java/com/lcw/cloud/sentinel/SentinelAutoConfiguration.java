package com.lcw.cloud.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcw.cloud.core.constant.CommonConstants;
import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.rest.entity.Result;
import com.lcw.cloud.feign.util.WebUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
public class SentinelAutoConfiguration {


    @Bean
    public RequestOriginParser requestOriginParser() {
        return httpServletRequest -> {
            String name = httpServletRequest.getHeader(CommonConstants.APP_NAME);
            if(StringUtils.isEmpty(name)){
                name= WebUtil.getIp(httpServletRequest);
            }
            return name;
        };
    }


    @Bean
    public BlockExceptionHandler blockExceptionHandler() {
        return (httpServletRequest, response, e) -> {
            //Sentinel规则的详细信息
            LogFactory.biz("BlockExceptionHandler BlockException Rule:{}" , e.getRule());

            Result r = null;
            if (e instanceof FlowException) {
                r = Result.error("100", "接口限流,请稍后重试");
            } else if (e instanceof DegradeException) {
                r = Result.error("101", "服务降级,请稍后重试");
            } else if (e instanceof ParamFlowException) {
                r = Result.error("102", "热点参数限流,请稍后重试");
            } else if (e instanceof SystemBlockException) {
                r = Result.error("103", "系统保护,请稍后重试");
            } else if (e instanceof AuthorityException) {
                r = Result.error("104", "授权规则不通过,请联系系统管理员处理");
            }
            //返回json数据
            response.setStatus(500);
            response.setCharacterEncoding("utf-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            new ObjectMapper().writeValue(response.getWriter(), r);
        };
    }
}

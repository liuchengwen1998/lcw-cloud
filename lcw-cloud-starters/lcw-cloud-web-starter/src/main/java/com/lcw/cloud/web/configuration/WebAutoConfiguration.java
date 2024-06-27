package com.lcw.cloud.web.configuration;


import com.lcw.cloud.web.exception.*;
import com.lcw.cloud.web.exception.notice.NoticeToOut;
import com.lcw.cloud.web.exception.notice.NoticeToOutContext;
import com.lcw.cloud.web.security.session.ISessionStore;
import com.lcw.cloud.web.security.session.RedisUserSessionStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Objects;

/**
 * web启动配置器
 * 必须在 {@link ErrorMvcAutoConfiguration} 配置类之前进行
 * 因为{@link ErrorMvcAutoConfiguration} 中也进行了
 * DefaultErrorAttributes 及 BasicErrorController 的初始化,需要使用我们自己的
 *
 * @author ：yzhang
 * @since ：2022/2/11 14:48
 */
@AutoConfigureBefore({ErrorMvcAutoConfiguration.class})
@ImportAutoConfiguration(WebAutoJacksonConfiguration.class)
public class WebAutoConfiguration {

    @Autowired(required = false)
    private List<NoticeToOut> noticeList;

    @Bean
    public DefaultErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes();
    }

    @Bean
    public NoticeToOutContext noticeToOutContext() {
        NoticeToOutContext noticeToOutContext = new NoticeToOutContext();
        if (Objects.nonNull(noticeList)) {
            noticeToOutContext.setNoticeList(noticeList);
        }
        return noticeToOutContext;
    }


    @Bean
    public GlobalExceptionController globalErrorController(ErrorAttributes errorAttributes) {
        return new GlobalExceptionController(errorAttributes);
    }

    @Bean
    public GlobalRestExceptionResolver globalRestExceptionResolver(NoticeToOutContext noticeToOutContext) {
        BaseRuntimeExceptionMapper baseRuntimeExceptionMapper = new BaseRuntimeExceptionMapper();
        baseRuntimeExceptionMapper.setNoticeToOutContext(noticeToOutContext);
        MethodArgumentTypeMismatchExceptionMapper methodArgumentTypeMismatchExceptionMapper = new MethodArgumentTypeMismatchExceptionMapper();
        methodArgumentTypeMismatchExceptionMapper.setNoticeToOutContext(noticeToOutContext);
        HttpMessageNotReadableExceptionMapper httpMessageNotReadableExceptionMapper = new HttpMessageNotReadableExceptionMapper();
        httpMessageNotReadableExceptionMapper.setNoticeToOutContext(noticeToOutContext);
        BindExceptionMapper bindExceptionMapper = new BindExceptionMapper();
        bindExceptionMapper.setNoticeToOutContext(noticeToOutContext);
        FeignBadExceptionMapper feignBadExceptionMapper = new FeignBadExceptionMapper();
        feignBadExceptionMapper.setNoticeToOutContext(noticeToOutContext);
        DefaultExceptionMapper defaultExceptionMapper = new DefaultExceptionMapper();
        defaultExceptionMapper.setNoticeToOutContext(noticeToOutContext);
        MethodArgumentNotValidExceptionMapper methodArgumentNotValidExceptionMapper = new MethodArgumentNotValidExceptionMapper();
        methodArgumentNotValidExceptionMapper.setNoticeToOutContext(noticeToOutContext);
        return new GlobalRestExceptionResolver()
                .setMapper(baseRuntimeExceptionMapper)
                .setMapper(methodArgumentNotValidExceptionMapper)
                .setMapper(new MissingServletRequestParameterExceptionMapper())
                .setMapper(new MissingRequestHeaderExceptionMapper())
                .setMapper(methodArgumentTypeMismatchExceptionMapper)
                .setMapper(new HttpRequestMethodNotSupportedExceptionMapper())
                .setMapper(httpMessageNotReadableExceptionMapper)
                .setMapper(new HttpMediaTypeNotSupportedExceptionMapper())
                .setMapper(bindExceptionMapper)
                .setMapper(feignBadExceptionMapper)
                .setMapper(new UnsupportedOperationExceptionMapper())
                .setMapper(defaultExceptionMapper);
    }

}

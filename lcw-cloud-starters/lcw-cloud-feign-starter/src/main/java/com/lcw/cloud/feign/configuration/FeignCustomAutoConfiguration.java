package com.lcw.cloud.feign.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.netflix.hystrix.*;
import com.lcw.cloud.feign.feign.FeignRequestHeaderInterceptor;
import com.lcw.cloud.feign.feign.HttpLoggingInterceptor;
import com.lcw.cloud.feign.feign.RestTemplateHeaderInterceptor;
import feign.Feign;
import feign.Request;
import feign.RequestInterceptor;
import feign.Target;
import feign.hystrix.HystrixFeign;
import feign.hystrix.SetterFactory;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.httpclient.OkHttpClientConnectionPoolFactory;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author yzhang
 * @desc springcloud 相关启动类
 * @since 2022/7/27
 */
@Configuration("hystrixFeignConfiguration")
@ConditionalOnClass({Feign.class, HystrixCommand.class, HystrixFeign.class})
@AutoConfigureAfter(EnableFeignClients.class)
@AllArgsConstructor
public class FeignCustomAutoConfiguration {


    @Resource
    private Environment config;
    private final ObjectMapper objectMapper;

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Feign.Builder feignHystrixBuilder(
            RequestInterceptor requestInterceptor) {
        SetterFactory setterFactory = new SetterFactory() {
            @Override
            public HystrixCommand.Setter create(Target<?> target, Method method) {
                String groupKey = target.name();
                String commandKey = Feign.configKey(target.type(), method);
                //HystrixThreadPoolProperties 线程池相关配置
                HystrixThreadPoolProperties.Setter setter = HystrixThreadPoolProperties.Setter().withCoreSize(Runtime.getRuntime().availableProcessors() * 2)
                        .withMaxQueueSize(200);
                //HystrixCommandProperties 熔断器相关属性配置 设置超时时间为60秒
                HystrixCommandProperties.Setter setter1 = HystrixCommandProperties.Setter()
                        .withExecutionTimeoutInMilliseconds(60000);

                return HystrixCommand.Setter
                        .withGroupKey(HystrixCommandGroupKey.Factory.asKey(groupKey))
                        .andCommandKey(HystrixCommandKey.Factory.asKey(commandKey))
                        .andThreadPoolPropertiesDefaults(setter)
                        .andCommandPropertiesDefaults(setter1);
            }
        };
        return HystrixFeign.builder()
                .contract(new SpringMvcContract())
                .decode404()
                .requestInterceptor(requestInterceptor)
                .setterFactory(setterFactory);
    }

    /**
     * 设置feign的连接超时时间和读超时时间，这里设置了就不会走Ribbon的超时时间了
     * @return
     */
    @Bean
    public Request.Options feignRequestOptions() {
        return new Request.Options(10000, 30000, true);
    }




    @Bean
    @ConditionalOnMissingBean
    public RequestInterceptor requestInterceptor() {
        String property = config.getProperty("spring.application.name");
        return new FeignRequestHeaderInterceptor(StringUtils.isNoneBlank(property)?property:"default");
    }

    @Bean
    public RestTemplateHeaderInterceptor requestHeaderInterceptor() {
        return new RestTemplateHeaderInterceptor();
    }

    @Bean
//    @LoadBalanced
    @ConditionalOnMissingBean(RestTemplate.class)
    public RestTemplate lbRestTemplate(okhttp3.OkHttpClient httpClient, RestTemplateHeaderInterceptor interceptor) {
        RestTemplate lbRestTemplate = new RestTemplate(new OkHttp3ClientHttpRequestFactory(httpClient));
        lbRestTemplate.setInterceptors(Collections.singletonList(interceptor));
        configMessageConverters(lbRestTemplate.getMessageConverters());
        return lbRestTemplate;
    }

    @Bean
    @ConditionalOnMissingBean(okhttp3.ConnectionPool.class)
    public okhttp3.ConnectionPool httpClientConnectionPool(
            FeignHttpClientProperties httpClientProperties,
            OkHttpClientConnectionPoolFactory connectionPoolFactory) {
        int maxTotalConnections = httpClientProperties.getMaxConnections();
        long timeToLive = httpClientProperties.getTimeToLive();
        TimeUnit ttlUnit = httpClientProperties.getTimeToLiveUnit();
        return connectionPoolFactory.create(maxTotalConnections, timeToLive, ttlUnit);
    }

    @Bean
    @ConditionalOnMissingBean(okhttp3.OkHttpClient.class)
    public okhttp3.OkHttpClient httpClient(
            OkHttpClientFactory httpClientFactory,
            okhttp3.ConnectionPool connectionPool,
            FeignHttpClientProperties httpClientProperties,
            HttpLoggingInterceptor interceptor) {
        boolean followRedirects = httpClientProperties.isFollowRedirects();
        return httpClientFactory.createBuilder(httpClientProperties.isDisableSslValidation())
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .followRedirects(followRedirects)
                .connectionPool(connectionPool)
                .addInterceptor(interceptor)
                .build();
    }

    @Bean
    public HttpLoggingInterceptor interceptor() {
        return new HttpLoggingInterceptor();
    }

    private void configMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.removeIf(x -> x instanceof StringHttpMessageConverter || x instanceof MappingJackson2HttpMessageConverter);
        converters.add(new StringHttpMessageConverter(Charsets.UTF_8));
        converters.add(new LcwMappingJackson2HttpMessageConverter(objectMapper));
    }

    class LcwMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
        public LcwMappingJackson2HttpMessageConverter(ObjectMapper objectMapper) {
            super(objectMapper);
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
            mediaTypes.add(MediaType.APPLICATION_JSON);
            setSupportedMediaTypes(mediaTypes);
        }
    }


}

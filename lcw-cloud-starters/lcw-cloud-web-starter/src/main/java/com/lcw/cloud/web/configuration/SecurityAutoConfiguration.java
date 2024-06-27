package com.lcw.cloud.web.configuration;

import com.lcw.cloud.web.security.filter.AuthenticationFilter;
import com.lcw.cloud.web.security.filter.SecurityProperties;
import com.lcw.cloud.web.security.intercept.UrlAccessDecisionManager;
import com.lcw.cloud.web.security.session.ISessionStore;
import com.lcw.cloud.web.security.session.RedisUserSessionStore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.web.servlet.ConditionalOnMissingFilterBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author yzhang
 * @since 2022/8/20
 */
@Configuration
@EnableConfigurationProperties({SecurityProperties.class})
public class SecurityAutoConfiguration {

    @Resource
    private SecurityProperties securityProperties;


    /**
     * 初始化会话保存
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ISessionStore.class)
    public ISessionStore sessionStore() {
        return new RedisUserSessionStore();
    }


    /**
     * 初始化安全过滤器
     *
     * @return
     */
    @Bean
    @ConditionalOnMissingFilterBean(AuthenticationFilter.class)
    @ConditionalOnProperty(prefix = "ygl.security", name = "enable", havingValue = "true")
    public FilterRegistrationBean<AuthenticationFilter> registrationBean() {
        FilterRegistrationBean<AuthenticationFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(authenticationFilter());
        registration.addUrlPatterns("/*");
        registration.setName("authenticationFilter");
        return registration;
    }

    /**
     * 构造认证类
     *
     * @return
     */
    public AuthenticationFilter authenticationFilter() {
        AuthenticationFilter filter =
                new AuthenticationFilter(securityProperties.getWhiteList(), new UrlAccessDecisionManager(), securityProperties.getOpenSign());
        filter.setSessionStore(sessionStore());
        return filter;
    }


}

package com.lcw.cloud.logger.configuration;

import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.logger.LoggerConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yzhang
 * @since 2022/7/27
 * 自动日志配置类
 */
@Configuration
@EnableConfigurationProperties(LoggerProperties.class)
public class LoggerAutoConfiguration {

    @Bean
    public LogFactory logFactory(LoggerProperties properties) {
        LogFactory.setProperties(new LoggerConfiguration(properties.getBizErr().getEnablePrint(), properties.getBizErr().getEnablePrintTraceLog()));
        return new LogFactory();
    }

}

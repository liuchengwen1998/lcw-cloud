package com.lcw.cloud.logger.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 日志配置属性类
 *
 * @author ：yzhang
 * @since ：2022/1/26 18:02
 */
@ConfigurationProperties(prefix = "lcw.cloud.logger")
@Data
public class LoggerProperties {

    /**
     * 业务错误日志
     */
    private BizErr bizErr = new BizErr();


    @Data
    public static class BizErr {

        /**
         * 是否输出日志堆栈信息,默认false，开发时可以打开
         */
        private Boolean enablePrint = Boolean.FALSE;

        /**
         * 是否输出请求日志信息，默认是开启，可以关闭
         */
        private Boolean enablePrintTraceLog = Boolean.TRUE;
    }

}

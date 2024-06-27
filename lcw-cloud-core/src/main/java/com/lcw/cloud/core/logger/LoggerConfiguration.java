package com.lcw.cloud.core.logger;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 日志配置属性类
 *
 * @author ：yzhang
 * @since ：2022/1/26 18:02
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoggerConfiguration {

    /**
     * 是否打印异常信息栈日志
     */
    private Boolean enablePrint = Boolean.FALSE;

    /**
     * 是否打印请求日志信息
     */
    private Boolean enablePrintTraceLog = Boolean.TRUE;

}

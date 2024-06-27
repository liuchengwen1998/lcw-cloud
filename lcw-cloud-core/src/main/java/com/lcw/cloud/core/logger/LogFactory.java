package com.lcw.cloud.core.logger;

import com.lcw.cloud.core.logger.biz.BizLog;
import com.lcw.cloud.core.logger.bizerr.BizErrLog;
import com.lcw.cloud.core.logger.syserr.SysErrLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志统一入口工程类
 *
 * @author ：yzhang
 * @since ：2022/1/26 17:37
 */
public class LogFactory {

    /**
     * 业务日志对象
     */
    private static final Logger bizLog = LoggerFactory.getLogger(BizLog.class);

    /**
     * 业务错误日志对象
     */
    private static final Logger bizErrLog = LoggerFactory.getLogger(BizErrLog.class);

    /**
     * 系统错误日志对象
     */
    private static final Logger sysErrLog = LoggerFactory.getLogger(SysErrLog.class);

    private static LoggerConfiguration properties;

    public static void setProperties(LoggerConfiguration properties) {
        LogFactory.properties = properties;
    }

    /**
     * @param msg  信息模板
     * @param args 模板参数
     * @Description 记录info级别的日志信息
     * @author yzhang
     */
    public static void biz(String msg, Object... args) {
        if (bizLog.isInfoEnabled()) {
            bizLog.info(msg, args);
        }
    }

    /**
     * @param msg  信息模板
     * @param args 模板参数
     * @Description 打印请求记录的日志信息
     * @author yzhang
     */
    public static void bizTraceLog(String msg, Object... args) {
        if (bizLog.isInfoEnabled() && properties.getEnablePrintTraceLog()) {
            bizLog.info(msg, args);
        }
    }

    /**
     * @param msg 错误信息标题
     * @param e   异常类
     * @Description 记录业务错误消息
     * @author yzhang
     */
    public static void bizErr(String msg, Throwable e) {
        if (properties.getEnablePrint()) {
            bizErrLog.error(msg, e);
        } else {
            bizErrLog.error(msg);
        }
    }

    /**
     * 打印异常日志
     *
     * @param msg
     */
    public static void bizErr(String msg) {
        bizErrLog.error(msg);
    }

    /**
     * 打印异常日志
     *
     * @param msg
     */
    public static void bizError(String msg, Object... args) {
        bizErrLog.error(msg, args);
    }

    /**
     * @param msg 错误信息标题
     * @param e   异常类
     * @Description 记录系统错误日志信息
     * @author yzhang
     */
    public static void err(String msg, Throwable e) {
        sysErrLog.error(msg, e);
    }
}

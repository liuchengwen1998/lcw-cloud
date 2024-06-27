package com.lcw.cloud.core.rest.constant;

/**
 * 错误信息常量类
 *
 * @author ：yzhang
 * @since ：2022/3/3 15:52
 */
public final class RestErrorMessage {

    public static final String MEDIA_TYPE_ERROR_MESSAGE = "ContentType[%s]设置错误。";

    public static final String REQUEST_BODY_UNCORRECTED_ERROR_MESSAGE = "请求包体格式错误，无法读取。";

    public static final String REQUEST_METHOD_NOT_SUPPORT_ERROR_MESSAGE = "请求方法【%s】不支持。";

    public static final String ARGUMENT_TYPE_MISMATCH_ERROR_MESSAGE = "参数【%s】类型错误。";

    public static final String REQUEST_HEADER_MISSING_ERROR_MESSAGE = "缺少Head参数【%s】。";

    public static final String REQUEST_PARAMETER_MISSING_ERROR_MESSAGE = "缺少请求参数【%s】。";


    /**
     * 格式化异常信息
     *
     * @param template 错误信息模板
     * @param args     参数
     * @return 最终的字符串
     */
    public static String format(String template, String... args) {
        return String.format(template, args);
    }

}

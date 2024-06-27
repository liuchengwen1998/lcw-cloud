package com.lcw.cloud.core.entity;

/**
 * @author yzhang
 * @since 2022/8/23
 * 常用的一些正则表达式
 */
public class CommonRegex {

    // 手机号正则
    public static final String MOBILE = "^1[3456789]\\d{9}$";

    // 邮箱
    public static final String EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    // 网址
    public static final String URL = "^[a-zA-z]+://(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*(\\?\\S*)?$";

    // 中文
    public static final String CHINESE = "[\\u4e00-\\u9fa5]+";

    // 身份证18位
    public static final String ID_CARD_18 = "^[1-9]\\d{5}(18|19|20|(3\\d))\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";

    // 身份证15位
    public static final String ID_CARD_15 = "^[1-9]\\d{5}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}$";

}

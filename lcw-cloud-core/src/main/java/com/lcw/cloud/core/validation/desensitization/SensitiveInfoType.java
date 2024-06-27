package com.lcw.cloud.core.validation.desensitization;

/**
 * SensitiveInfoType 敏感信息类型
 *
 * @author ：yzhang
 * @date ：2022/3/14 16:21
 */
public class SensitiveInfoType {

    private SensitiveInfoType() {
        throw new IllegalArgumentException("不可实例化");
    }

    /**
     * 姓名
     */
    public static final String REAL_NAME = "1";

    /**
     * 身份证
     */
    public static final String ID_CARD = "2";

    /**
     * 手机号
     */
    public static final String MOBILE = "4";

    /**
     * 固话
     */
    public static final String TEL = "5";

    /**
     * 邮箱
     */
    public static final String EMAIL = "6";

    /**
     * 银行卡号
     */
    public static final String BANK_CARD_NO = "7";

    /**
     * 其它信息
     */
    public static final String OTHER = "99";
}

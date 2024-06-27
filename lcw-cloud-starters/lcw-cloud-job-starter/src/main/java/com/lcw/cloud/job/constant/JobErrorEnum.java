package com.lcw.cloud.job.constant;

import com.lcw.cloud.core.dict.IEnum;

/**
 * @author yzhang
 * @since 2022/8/2
 */
public enum JobErrorEnum implements IEnum {
    PLEASE_CONFIG_XXL_JOB_CONFIG("11001", "请配置XXL-JOB相关的配置信息");

    private String code;
    private String msg;

    JobErrorEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}

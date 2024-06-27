package com.lcw.cloud.core.dict;

/**
 * @author yzhang
 * @since 2022/7/27
 * @desc 枚举抽象类
 */
public interface IEnum {

    /**
     * 代号
     * @return
     */
    String getCode();

    /**
     * 备注
     * @return
     */
    String getMsg();

}

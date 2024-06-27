package com.lcw.cloud.mq.configuration.constant;

import com.lcw.cloud.core.dict.IEnum;

/**
 * @author yzhang
 * @since 2022/8/2
 */
public enum MqErrorEnum implements IEnum {
    SEND_MSG_FAILED("10001", "发送MQ消息失败"),
    SEND_MSG_CALLBACK_HANDLE_FAILED("10002", "发送MQ消息回调处理失败"),
    NOT_FOUND_ROCKETMQ_CONFIG("10003", "暂未找到对应的mq对应的key"),
    NOT_FOUNT_ROCKETMQ_CONFIG("10004", "尚未配置RocketMQ配置"),
    ROCKETMQ_CONFIG_KEY_REPEAT("10005", "rocketMQ的配置项key不能重复"),
    NOT_SUPPORT_OUT_CREATE_PRODUCE("10006", "暂不支持外部创建MQProduce");

    private String code;
    private String msg;

    MqErrorEnum(String code, String msg) {
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

package com.lcw.cloud.mq.configuration.producer;

/**
 * 消息成功发送回调
 */
public interface RocketMessageCallBack {
    void call(String messageId);
}

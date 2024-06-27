package com.lcw.cloud.mq.configuration.producer;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.exception.ONSClientException;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.lcw.cloud.core.exception.BizException;
import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.mq.configuration.constant.MqErrorEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;
import java.util.UUID;

/**
 * @Description:
 * @author: wangjinhui
 * @Date: 2020/7/9 10:57
 */
@Data
@AllArgsConstructor
public class ProducerOrderClient {

    private OrderProducer orderProducer;


    /**
     * 发送MQ 消息 ,消息体为 String
     *
     * @param topic
     * @param tag
     * @param body
     */
    public void buildMessage(String topic, String tag, String body, String shardingKey) {
        Message msg = new Message(topic, tag, body.getBytes());
        msg.setKey(UUID.randomUUID().toString());
        buildMessage(msg, NumberUtils.INTEGER_ZERO, shardingKey, null);
    }

    /**
     * 发送MQ 消息 ,消息体为 String, 消息发送成功，执行回调函数
     *
     * @param topic
     * @param tag
     * @param body
     * @param rocketMessageCallBack
     * @since 1.0.8
     */
    public void buildMessage(String topic, String tag, String body, String shardingKey, RocketMessageCallBack rocketMessageCallBack) {
        Message msg = new Message(topic, tag, body.getBytes());
        msg.setKey(UUID.randomUUID().toString());
        buildMessage(msg, NumberUtils.INTEGER_ZERO, shardingKey, rocketMessageCallBack);
    }

    /**
     * 发送MQ 消息 ,消息体为 Object 类型， 会转成JSON 类型发送
     *
     * @param topic
     * @param tag
     * @param body
     * @param rocketMessageCallBack
     * @since 1.0.8
     */
    public void buildMessage(String topic, String tag, Object body, String shardingKey, RocketMessageCallBack rocketMessageCallBack) {
        buildMessage(topic, tag, JSONObject.toJSONString(body), shardingKey, rocketMessageCallBack);
    }

    public void buildMessage(String topic, String tag, Object body, String shardingKey) {
        buildMessage(topic, tag, JSONObject.toJSONString(body), shardingKey, null);
    }

    /**
     * 发送延迟队列消息，topic消息类型必须是延迟类型
     *
     * @param topic
     * @param tag
     * @param body
     * @param delayTime
     * @param rocketMessageCallBack
     * @since 1.0.8
     */
    public void buildDelayMessage(String topic, String tag, String body, long delayTime, String shardingKey, RocketMessageCallBack rocketMessageCallBack) {
        Message msg = new Message(topic, tag, body.getBytes());
        msg.setKey(UUID.randomUUID().toString());
        msg.setStartDeliverTime(System.currentTimeMillis() + delayTime);
        buildMessage(msg, NumberUtils.INTEGER_ZERO, shardingKey, rocketMessageCallBack);
    }

    /**
     * 发送延迟队列消息，topic消息类型必须是延迟类型
     *
     * @param topic
     * @param tag
     * @param body
     * @param delayTime
     * @since 1.0.8
     */
    public void buildDelayMessage(String topic, String tag, Object body, long delayTime, String shardingKey) {
        buildDelayMessage(topic, tag, JSONObject.toJSONString(body), delayTime, null);
    }

    private void buildMessage(Message msg, Integer num, String shardingKey, RocketMessageCallBack rocketMessageCallBack) {
        try {
            SendResult sendResult = orderProducer.send(msg, shardingKey);
            assert sendResult != null;
            if (Objects.nonNull(rocketMessageCallBack)) {
                try {
                    rocketMessageCallBack.call(sendResult.getMessageId());
                } catch (Exception e) {
                    LogFactory.bizErr("mq 回调捕获异常:", e);
                    throw new BizException(MqErrorEnum.SEND_MSG_CALLBACK_HANDLE_FAILED.getCode(), MqErrorEnum.SEND_MSG_CALLBACK_HANDLE_FAILED.getMsg());
                }
            }
            LogFactory.biz(" Send mq message success! Topic is:" + msg.getTopic() + " msgId is: " + sendResult.getMessageId());
        } catch (ONSClientException e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            LogFactory.bizErr(" Send mq message failed! Topic is:" + msg.getTopic() + ",  retry times is " + num, e);
            if (num <= NumberUtils.INTEGER_TWO) {
                buildMessage(msg, ++num, shardingKey, rocketMessageCallBack);
            } else {
                throw new BizException(MqErrorEnum.SEND_MSG_FAILED.getCode(), MqErrorEnum.SEND_MSG_FAILED.getMsg());
            }
        }
    }
}

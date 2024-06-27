package com.lcw.cloud.mq.configuration.props;

import lombok.Data;

/**
 * @author wangjinhui
 * @date 2020/7/21
 */
@Data
//@ConfigurationProperties(prefix ="ygl.rocketmq")
public class RocketProperties {

    /**
     * 当前配置的唯一标识，默认为default
     */
    private String key = "default";

    /**
     * AccessKeyId 阿里云身份验证，在阿里云用户信息管理控制台获取。
     */
    private String accessKey;

    /**
     * AccessKeySecret 阿里云身份验证，在阿里云用户信息管理控制台获取。
     */
    private String secretKey;

    /**
     * 设置 TCP 接入域名，进入控制台的实例详情页面的 TCP 协议客户端接入点区域查看。
     */
    private String nameSrvAddr;

    /**
     * 设置消息发送的超时时间，单位（毫秒），默认：3000
     */
    private Integer sendMsgTimeoutMillis = 3000;
    /**
     * 设置事务消息第一次回查的最快时间，单位（秒）
     */
    private Integer checkImmunityTimeInSeconds = 5;
    /**
     * 设置 RocketMessage 实例的消费线程数，阿里云默认：20
     * 默认cpu数量*2+1
     */
    private Integer consumeThreadNums = Runtime.getRuntime().availableProcessors() * 2 + 1;
    /**
     * 设置消息消费失败的最大重试次数，默认：16
     */
    private Integer maxReconsumeTimes = 16;
    /**
     * 设置每条消息消费的最大超时时间，超过设置时间则被视为消费失败，等下次重新投递再次消费。 每个业务需要设置一个合理的值，单位（分钟）。 默认：15
     */
    private Integer consumeTimeout = 15;
    /**
     * 只适用于顺序消息，设置消息消费失败的重试间隔时间默认100毫秒
     */
    private Integer suspendTimeMilli = 100;
    /**
     * 异步发送消息执行Callback的目标线程池
     */
    private Integer callbackThreadNums = Runtime.getRuntime().availableProcessors() * 2 + 1;
    /**
     * 初始化消费者线程数，（尽量和消费者数量一致）默认cpu数量*2+1
     */
    private Integer createConsumeThreadNums = Runtime.getRuntime().availableProcessors() * 2 + 1;
    /**
     * 初始化生产者线程数，（尽量和生产者数量一致）默认cpu数量*2+1
     */
    private Integer createProducerThreadNums = Runtime.getRuntime().availableProcessors() * 2 + 1;
    /**
     * 生产者发送消息线程数 默认cpu数量*2+1
     */
    private Integer sendMessageThreadNums = Runtime.getRuntime().availableProcessors() * 2 + 1;

}

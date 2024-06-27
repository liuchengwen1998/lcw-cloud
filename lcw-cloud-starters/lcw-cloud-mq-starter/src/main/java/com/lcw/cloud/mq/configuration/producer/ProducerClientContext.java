package com.lcw.cloud.mq.configuration.producer;

import com.lcw.cloud.core.base.Preconditions;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.lcw.cloud.mq.configuration.constant.MqErrorEnum.NOT_FOUND_ROCKETMQ_CONFIG;
import static com.lcw.cloud.mq.configuration.constant.MqErrorEnum.NOT_SUPPORT_OUT_CREATE_PRODUCE;

/**
 * @author yzhang
 * @since 2022/8/4
 * mq上下文配置,不允许继承修改
 */
public final class ProducerClientContext {

    // 普通
    private static final Map<String/*key*/, ProducerClient> clientMap = new ConcurrentHashMap<>(8);

    // 顺序
    private static final Map<String/*key*/, ProducerOrderClient> orderClientMap = new ConcurrentHashMap<>(8);


    /**
     * 添加Mq生产者,只有内部代码才能创建{@link com.lcw.cloud.mq.configuration.config.RocketAutoConfiguration}
     *
     * @param key
     * @param client
     */
    public static void put(String key, ProducerClient client) {
        String classname = new Exception().getStackTrace()[1].getClassName();
        Preconditions.checkState("com.lcw.cloud.mq.configuration.config.RocketAutoConfiguration".equals(classname),
                NOT_SUPPORT_OUT_CREATE_PRODUCE);
        clientMap.put(key, client);
    }

    /**
     * 添加创建顺序生产者
     *
     * @param key
     * @param client
     */
    public static void putOrder(String key, ProducerOrderClient client) {
        String classname = new Exception().getStackTrace()[1].getClassName();
        Preconditions.checkState("com.lcw.cloud.mq.configuration.config.RocketAutoConfiguration".equals(classname),
                NOT_SUPPORT_OUT_CREATE_PRODUCE);
        orderClientMap.put(key, client);
    }

    /**
     * 获取默认的，一般默认的都是配置成公司的
     *
     * @return
     */
    public static ProducerClient getDefault() {
        return get("default");
    }

    /**
     * 获取默认的，一般默认的都是配置成公司的
     *
     * @return
     */
    public static ProducerOrderClient getOrderDefault() {
        return getOrder("default");
    }

    /**
     * 获取Mq生产者客户端
     *
     * @param key
     * @return
     */
    public static ProducerClient get(String key) {
        ProducerClient producerClient = clientMap.get(key);
        Preconditions.checkNotNull(producerClient, NOT_FOUND_ROCKETMQ_CONFIG);
        return producerClient;
    }


    /**
     * 获取Mq生产者顺序客户端
     *
     * @param key
     * @return
     */
    public static ProducerOrderClient getOrder(String key) {
        ProducerOrderClient producerClient = orderClientMap.get(key);
        Preconditions.checkNotNull(producerClient, NOT_FOUND_ROCKETMQ_CONFIG);
        return producerClient;
    }


}

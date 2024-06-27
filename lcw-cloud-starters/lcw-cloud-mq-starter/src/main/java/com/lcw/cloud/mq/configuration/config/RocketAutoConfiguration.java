package com.lcw.cloud.mq.configuration.config;

import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.Producer;
import com.aliyun.openservices.ons.api.order.OrderProducer;
import com.lcw.cloud.mq.configuration.container.RocketConsumerContainer;
import com.lcw.cloud.mq.configuration.factory.PropertiesFactory;
import com.lcw.cloud.mq.configuration.producer.ProducerClient;
import com.lcw.cloud.mq.configuration.producer.ProducerClientContext;
import com.lcw.cloud.mq.configuration.producer.ProducerOrderClient;
import com.lcw.cloud.mq.configuration.props.RocketPropertiesConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Properties;

/**
 * @author wangjinhui
 * @date 2020/7/29
 */
@Configuration
@EnableConfigurationProperties(RocketPropertiesConfig.class)
public class RocketAutoConfiguration {


    @PostConstruct
    private void init() {
        // 启动配置项校验
        rocketPropertiesConfig.checkKeyRepeat();

        // 启动mq生产者列表
        rocketPropertiesConfig.getList().forEach(e -> {
            Properties properties = PropertiesFactory.createProperties(e);
            Producer producer = ONSFactory.createProducer(properties);
            // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可。
            producer.start();
            ProducerClient producerClient = new ProducerClient(producer);
            ProducerClientContext.put(e.getKey(), producerClient);

            OrderProducer orderProducer = ONSFactory.createOrderProducer(properties);
            // 在发送消息前，必须调用 start 方法来启动 Producer，只需调用一次即可。
            orderProducer.start();
            ProducerOrderClient producerOrderClient = new ProducerOrderClient(orderProducer);
            ProducerClientContext.putOrder(e.getKey(), producerOrderClient);
        });
    }

    @Resource
    private RocketPropertiesConfig rocketPropertiesConfig;

    @Bean
    @ConditionalOnMissingBean(RocketConsumerContainer.class)
    public RocketConsumerContainer rocketConsumerContainer() {
        return new RocketConsumerContainer(rocketPropertiesConfig);
    }

}

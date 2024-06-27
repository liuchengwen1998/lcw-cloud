package com.lcw.cloud.mq.configuration.factory;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.lcw.cloud.mq.configuration.annotation.MessageListener;
import com.lcw.cloud.mq.configuration.props.RocketProperties;

import java.util.Properties;


/**
 * ClassName: ConsumerFactory
 * Description:
 * date: 2019/4/27 15:55
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
public class ConsumerFactory {
    private ConsumerFactory() {
    }

    public static Properties createConsumerProperties(RocketProperties rocketProperties, MessageListener messageListener) {

        Properties properties = PropertiesFactory.createProperties(rocketProperties);

        properties.put(PropertyKeyConst.GROUP_ID, messageListener.groupID());
        properties.put(PropertyKeyConst.ConsumeThreadNums, rocketProperties.getConsumeThreadNums());
        properties.put(PropertyKeyConst.MaxReconsumeTimes, rocketProperties.getMaxReconsumeTimes());
        properties.put(PropertyKeyConst.ConsumeTimeout, rocketProperties.getConsumeTimeout());


        return properties;

    }

    public static Consumer createConsumer(Properties properties) {
        return ONSFactory.createConsumer(properties);
    }

    public static OrderConsumer createOrderConsumer(Properties properties) {
        return ONSFactory.createOrderedConsumer(properties);
    }
}

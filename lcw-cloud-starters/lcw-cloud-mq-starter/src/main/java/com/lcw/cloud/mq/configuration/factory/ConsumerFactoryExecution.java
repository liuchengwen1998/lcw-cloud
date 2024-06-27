package com.lcw.cloud.mq.configuration.factory;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.order.OrderConsumer;
import com.lcw.cloud.mq.configuration.annotation.MessageListener;
import com.lcw.cloud.mq.configuration.annotation.RocketListener;
import com.lcw.cloud.mq.configuration.consumer.DefaultMessageListener;
import com.lcw.cloud.mq.configuration.consumer.DefaultMessageOrderListener;
import com.lcw.cloud.mq.configuration.props.RocketProperties;
import com.lcw.cloud.mq.configuration.thread.AbstractConsumerThread;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * ClassName: ConsumerFactoryExecution
 * Description:
 * date: 2019/4/27 16:05
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
public class ConsumerFactoryExecution extends AbstractConsumerThread {


    public ConsumerFactoryExecution(RocketProperties rocketProperties, RocketListener rocketListener, MessageListener consumerListener, MethodFactoryExecution methodFactoryExecution) {
        super(rocketProperties, rocketListener, consumerListener, methodFactoryExecution);
    }

    /**
     * 开始启动consumer
     * @param rocketProperties       rocketProperties
     * @param rocketListener         rocketListener
     * @param messageListener       consumerListener
     * @param methodFactoryExecution methodFactoryExecution
     */
    @Override
    public void statsConsumer(RocketProperties rocketProperties,
                              RocketListener rocketListener,
                              MessageListener messageListener,
                              MethodFactoryExecution methodFactoryExecution) {
        Properties properties = ConsumerFactory.createConsumerProperties(rocketProperties, messageListener);
        if (StringUtils.isEmpty(messageListener.groupID())) {
            properties.put(PropertyKeyConst.GROUP_ID, rocketListener.groupID());
        }
        properties.put(PropertyKeyConst.MessageModel, rocketListener.messageModel());
        properties.put(PropertyKeyConst.MaxReconsumeTimes, messageListener.retryCount());
        String topic = StringUtils.isEmpty(messageListener.topic()) ? rocketListener.topic() : messageListener.topic();
        if (rocketListener.orderConsumer()) {
            properties.put(PropertyKeyConst.SuspendTimeMillis, rocketProperties.getSuspendTimeMilli());
            OrderConsumer orderConsumer = ConsumerFactory.createOrderConsumer(properties);
            orderConsumer.subscribe(topic, messageListener.tag(), new DefaultMessageOrderListener(methodFactoryExecution));
            orderConsumer.start();
            return;
        }
        Consumer consumer = ConsumerFactory.createConsumer(properties);
        consumer.subscribe(topic, messageListener.tag(), new DefaultMessageListener(methodFactoryExecution));
        consumer.start();

    }
}

package com.lcw.cloud.mq.configuration.thread;

import com.lcw.cloud.mq.configuration.annotation.MessageListener;
import com.lcw.cloud.mq.configuration.annotation.RocketListener;
import com.lcw.cloud.mq.configuration.factory.MethodFactoryExecution;
import com.lcw.cloud.mq.configuration.props.RocketProperties;
import lombok.Data;

/**
 * ClassName: AbstractConsumerThread
 * Description:
 * date: 2019/4/27 20:03
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Data
public abstract class AbstractConsumerThread implements Runnable {
    private RocketProperties rocketProperties;
    private RocketListener rocketListener;
    private MessageListener consumerListener;
    private MethodFactoryExecution methodFactoryExecution;

    public AbstractConsumerThread(RocketProperties rocketProperties, RocketListener rocketListener, MessageListener consumerListener, MethodFactoryExecution methodFactoryExecution) {
        this.rocketProperties = rocketProperties;
        this.rocketListener = rocketListener;
        this.consumerListener = consumerListener;
        this.methodFactoryExecution = methodFactoryExecution;
    }

    /**
     * 消费者开始监听
     *
     * @param rocketProperties       rocketProperties
     * @param consumerListener       consumerListener
     * @param rocketListener         rocketListener
     * @param methodFactoryExecution methodFactoryExecution
     */
    protected abstract void statsConsumer(RocketProperties rocketProperties,
                                          RocketListener rocketListener,
                                          MessageListener consumerListener,
                                          MethodFactoryExecution methodFactoryExecution);

    @Override
    public void run() {
        statsConsumer(this.getRocketProperties(),
                this.getRocketListener(),
                this.getConsumerListener(),
                this.getMethodFactoryExecution());
    }
}

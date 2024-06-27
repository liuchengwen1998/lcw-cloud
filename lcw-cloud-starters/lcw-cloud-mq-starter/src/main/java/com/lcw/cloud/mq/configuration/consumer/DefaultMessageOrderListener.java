

package com.lcw.cloud.mq.configuration.consumer;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.mq.configuration.factory.MethodFactoryExecution;
import lombok.extern.slf4j.Slf4j;

/**
 * ClassName: DefaultMessageOrderListener
 * Description:
 * date: 2019/4/26 23:16
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */

@Slf4j
public class DefaultMessageOrderListener extends AbstractRocketListener implements MessageOrderListener {


    public DefaultMessageOrderListener(MethodFactoryExecution methodFactoryExecution) {
        super(methodFactoryExecution);
    }

    /**
     * 消费消息接口，由应用来实现<br>
     * 需要注意网络抖动等不稳定的情形可能会带来消息重复，对重复消息敏感的业务可对消息做幂等处理
     *
     * @param message 消息
     * @param context 消费上下文
     * @return {@link OrderAction} 消费结果，如果应用抛出异常或者返回Null等价于返回Action.ReconsumeLater
     * @see <a href="https://help.aliyun.com/document_detail/44397.html">如何做到消费幂等</a>
     */
    @Override
    public OrderAction consume(Message message, ConsumeOrderContext context) {
        log.info(">>>>topic is {}, Order message:{}>>>>",message.getTopic(), message);
        try {
            super.getMethodFactoryExecution().methodExecution(message);
        } catch (Exception e) {
            LogFactory.bizErr("order 消费消息发生异常，消费的topic为" + message.getTopic()
                    + ",消费的bean为" + methodFactoryExecution.getBean()
                    + ",消费的方法为" + methodFactoryExecution.getMethod(), e);
            return OrderAction.Suspend;
        }
        return OrderAction.Success;
    }
}

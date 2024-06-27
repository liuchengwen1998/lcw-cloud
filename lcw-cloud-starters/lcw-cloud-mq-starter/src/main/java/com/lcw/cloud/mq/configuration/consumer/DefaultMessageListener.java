
package com.lcw.cloud.mq.configuration.consumer;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.mq.configuration.factory.MethodFactoryExecution;

/**
 * ClassName: DefaultMessageListener
 * Description:
 * date: 2019/4/27 17:05
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
public class DefaultMessageListener extends AbstractRocketListener implements MessageListener {

    public DefaultMessageListener(MethodFactoryExecution methodFactoryExecution) {
        super(methodFactoryExecution);
    }

    /**
     * 消费消息接口，由应用来实现<br>
     * 网络抖动等不稳定的情形可能会带来消息重复，对重复消息敏感的业务可对消息做幂等处理
     *
     * @param message 消息
     * @param context 消费上下文
     * @return 消费结果，如果应用抛出异常或者返回Null等价于返回Action.ReconsumeLater
     * @see <a href="https://help.aliyun.com/document_detail/44397.html">如何做到消费幂等</a>
     */
    @Override
    public Action consume(Message message, ConsumeContext context) {
        LogFactory.biz(">>>>topic is {}, message:{}>>>>", message.getTopic(), message);
        try {
            super.getMethodFactoryExecution().methodExecution(message);
        } catch (Exception e) {
            LogFactory.bizErr("消费消息发生异常，消费的topic为" + message.getTopic()
                    + ",消费的bean为" + methodFactoryExecution.getBean()
                    + ",消费的方法为" + methodFactoryExecution.getMethod(), e);
            return Action.ReconsumeLater;
        }
        return Action.CommitMessage;
    }
}

package com.lcw.cloud.mq.configuration.annotation;

import com.aliyun.openservices.ons.api.PropertyValueConst;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * ClassName: MessageListener
 * Description:
 * date: 2019/4/26 22:37
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface MessageListener {

    /**
     * Message 所属的 Topic
     *
     * @return String
     */
    String topic() default "";

    /**
     * 您在控制台创建的 Group ID
     *
     * @return String
     */
    String groupID() default "";

    /**
     * 订阅指定 Topic 下的 Tags：
     * 1. * 表示订阅所有消息
     * 2. TagA || TagB || TagC 表示订阅 TagA 或 TagB 或 TagC 的消息
     *
     * @return String
     */
    String tag() default "*";

    /**
     * 消费模式，默认集群消费
     *
     * @return String
     */
    String messageModel() default PropertyValueConst.CLUSTERING;

    /**
     * 重试次数
     */
    int retryCount() default 16;
}

package com.lcw.cloud.mq.configuration.annotation;

import com.aliyun.openservices.ons.api.PropertyValueConst;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * ClassName: RocketListener
 * Description:
 * date: 2019/4/26 21:35
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Component
public @interface RocketListener {

    /**
     * 默认的mq配置项，一般都是配置成公司内部使用的
     * @return
     */
    String key() default "default";
    
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
     * 消费模式，默认集群消费
     *
     * @return String
     */
    String messageModel() default PropertyValueConst.CLUSTERING;

    /**
     * 是否为顺序消息
     *
     * @return Boolean
     */

    boolean orderConsumer() default false;
}

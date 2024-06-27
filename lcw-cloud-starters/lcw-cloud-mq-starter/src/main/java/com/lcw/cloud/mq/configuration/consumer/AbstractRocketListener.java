
package com.lcw.cloud.mq.configuration.consumer;

import com.lcw.cloud.mq.configuration.factory.MethodFactoryExecution;
import lombok.Data;

/**
 * ClassName: AbstractRocketListener
 * Description:
 * date: 2019/4/27 17:07
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Data
public abstract class AbstractRocketListener {
    protected MethodFactoryExecution methodFactoryExecution;

    public AbstractRocketListener(MethodFactoryExecution methodFactoryExecution) {
        this.methodFactoryExecution = methodFactoryExecution;
    }

}

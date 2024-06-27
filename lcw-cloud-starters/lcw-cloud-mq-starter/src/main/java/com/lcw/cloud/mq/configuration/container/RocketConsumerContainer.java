/**
 * Copyright 2019 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lcw.cloud.mq.configuration.container;


import com.lcw.cloud.mq.configuration.annotation.MessageListener;
import com.lcw.cloud.mq.configuration.annotation.RocketListener;
import com.lcw.cloud.mq.configuration.factory.ConsumerFactoryExecution;
import com.lcw.cloud.mq.configuration.factory.MethodFactoryExecution;
import com.lcw.cloud.mq.configuration.factory.ThreadPoolExecutorExecution;
import com.lcw.cloud.mq.configuration.factory.ThreadPoolFactory;
import com.lcw.cloud.mq.configuration.props.RocketPropertiesConfig;
import com.lcw.cloud.mq.configuration.utils.AnnotatedMethodsUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * ClassName: RocketConsumerContainer
 * Description:
 * date: 2019/4/26 21:40
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
public class RocketConsumerContainer implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    private RocketPropertiesConfig rocketPropertiesConfig;

    public RocketConsumerContainer(RocketPropertiesConfig rocketPropertiesConfig) {
        this.rocketPropertiesConfig = rocketPropertiesConfig;
    }

    @PostConstruct
    public void initialize() {
        ThreadPoolExecutor threadPoolExecutor = ThreadPoolFactory.createConsumeThreadPoolExecutor(rocketPropertiesConfig.getDefault());

       // 查找所有实现了RocketListener注解的bean，实例化mq的consumer
        applicationContext.getBeansWithAnnotation(RocketListener.class).forEach((beanName, bean) -> {
            RocketListener rocketListener = bean.getClass().getAnnotation(RocketListener.class);
            AnnotatedMethodsUtils.getMethodAndAnnotation(bean, MessageListener.class).
                    forEach((method, consumerListener) -> {
                        ConsumerFactoryExecution consumerFactoryExecution =
                                new ConsumerFactoryExecution(rocketPropertiesConfig.getProperties(rocketListener.key()),
                                rocketListener, consumerListener, new MethodFactoryExecution(bean, method));
                        ThreadPoolExecutorExecution.statsThread(threadPoolExecutor, consumerFactoryExecution);
                    });
        });

        threadPoolExecutor.shutdown();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}

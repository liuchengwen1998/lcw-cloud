package com.lcw.cloud.mq.configuration.factory;

import com.aliyun.openservices.ons.api.Message;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * ClassName: MethodFactoryExecution
 * Description:
 * date: 2019/4/27 16:26
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
@Data
@Slf4j
public class MethodFactoryExecution {
    private Object bean;
    private Method method;


    public MethodFactoryExecution(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public void methodExecution(Message message) throws Exception {
        try {
            method.invoke(bean, message);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}

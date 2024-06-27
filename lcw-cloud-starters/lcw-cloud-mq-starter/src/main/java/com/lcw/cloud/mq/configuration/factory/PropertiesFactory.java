package com.lcw.cloud.mq.configuration.factory;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.lcw.cloud.mq.configuration.props.RocketProperties;

import java.util.Properties;

/**
 * ClassName: PropertiesFactory
 * Description:
 * date: 2019/4/27 20:26
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
public class PropertiesFactory {
    private PropertiesFactory() {
    }

    public static Properties createProperties(RocketProperties rocketProperties) {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.NAMESRV_ADDR, rocketProperties.getNameSrvAddr());
        properties.put(PropertyKeyConst.AccessKey, rocketProperties.getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, rocketProperties.getSecretKey());
        return properties;
    }
}

package com.lcw.cloud.mq.configuration.props;

import com.lcw.cloud.core.base.Preconditions;
import com.lcw.cloud.core.exception.BizException;
import com.lcw.cloud.core.logger.LogFactory;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import static com.lcw.cloud.mq.configuration.constant.MqErrorEnum.*;

/**
 * @author yzhang
 * @since 2022/8/4
 * rocketmq配置项
 */
@Data
@Configuration
@ConfigurationProperties(prefix = RocketPropertiesConfig.ROCKET_PREFIX)
public class RocketPropertiesConfig {

    public static final String ROCKET_PREFIX = "lcw.rocketmq";
    private List<RocketProperties> list;

    public RocketProperties getDefault() {
        return getProperties("default");
    }

    public RocketProperties getProperties(String key) {
        LogFactory.biz("请求的key为{}", key);
        return list.stream().filter(e -> e.getKey().equals(key))
                .findFirst()
                .orElseThrow(() -> new BizException(NOT_FOUND_ROCKETMQ_CONFIG.getCode(), NOT_FOUND_ROCKETMQ_CONFIG.getMsg()));
    }

    /**
     * 校验key是否重复
     */
    public void checkKeyRepeat() {
        long count = list.stream().map(RocketProperties::getKey).distinct().count();
        Preconditions.checkState(count != 0, NOT_FOUNT_ROCKETMQ_CONFIG);
        Preconditions.checkState(count == list.size(), ROCKETMQ_CONFIG_KEY_REPEAT);
    }

}

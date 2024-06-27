package com.lcw.cloud.web.security.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yzhang
 * @since 2022/8/20
 * 安全配置类
 */
@ConfigurationProperties(prefix = "ygl.security")
@Data
public class SecurityProperties {

    /**
     * 白名单列表
     */
    private List<String> whiteList = new ArrayList<>();

    /**
     * 开启验签
     */
    private Boolean openSign = false;

    /**
     * 是否开启URL权限认证，默认开启，只有开发和测试的时候可以关闭
     */
    private Boolean urlDecisionEnable = true;



}

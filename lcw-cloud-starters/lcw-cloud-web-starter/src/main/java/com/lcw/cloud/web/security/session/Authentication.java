package com.lcw.cloud.web.security.session;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author yzhang
 * @since 2022/8/9
 * 会话信息
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Authentication implements Serializable {


    private static final long serialVersionUID = -189870674824249842L;

    /**
     * 当前子系统
     */
    private String subSystemCode;

    /**
     * 用户编号
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 驿公里用户编号
     */
    private Long yglUserId;

    /**
     * 钉用户编号
     */
    private String dingUserId;

    /**
     * 用户状态
     */
    private String userStatus;

    /**
     * 用户头像
     */
    private String portrait;

    /**
     * 用户权限列表
     */
    private Map<String, List<String>> authority;

    private String accessToken;

    private Date accessTokenExpire;

    private String refreshToken;

    private Date refreshTokenExpire;

    /**
     * app键，开放接口专属
     */
    private String appKey;

    /**
     * 应用唯一编号，开放接口专属
     */
    private Long appUniqueId;

    /**
     * 工位编号列表，开放接口专属
     */
    private List<String> stationIdList;

}

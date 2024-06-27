package com.lcw.cloud.web.yglsecure;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wangjinhui
 * @date 2020/7/23
 */
@Data
public class YglUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 邮箱
     */
    private String mail;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * 用户名
     */
    private String username;
    /**
     * jwt token
     */
    private String accessToken;

}

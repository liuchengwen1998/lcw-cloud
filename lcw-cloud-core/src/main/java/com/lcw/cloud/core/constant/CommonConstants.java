package com.lcw.cloud.core.constant;

import com.lcw.cloud.core.util.MD5Utils;

/**
 * 基础常量类
 *
 * @author ：yzhang
 * @since ：2022/3/31 16:54
 */
public interface CommonConstants {

    String COMMA = ",";

    String SIGN_KEY = "ygl15a";
    String ACCESS_TOKEN = "accessToken";
    String APP_HEADER = "app-token";
    String APP_NAME = "app-name";
    String FULL_HEADER = "full-token";
    String APP_HEADER_TOKEN = MD5Utils.md5HexSimple(SIGN_KEY);
    String TOKEN = "Authorization";
    String HEADER = "header";
    String EXPIRES_IN = "expiresIn";
    String USER_ID = "userId";
    String MOBILE = "mobile";
    String MAIL = "mail";
    String NICK_NAME = "nickName";


}

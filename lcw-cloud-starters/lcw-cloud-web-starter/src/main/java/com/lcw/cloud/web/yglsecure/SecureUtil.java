package com.lcw.cloud.web.yglsecure;

import com.google.common.base.Charsets;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * @Description:
 * @author: wangjinhui
 * @Date: 2020/6/2 11:03
 */
@Slf4j
@Component
public class SecureUtil {
    private static final String YGL_USER_REQUEST_ATTR = "_YGL_USER_REQUEST_ATTR_";

    private final static String HEADER = "accessToken";

    private static String BASE64_SECURITY = Base64.getEncoder().encodeToString("ygl15a".getBytes(Charsets.UTF_8));

    /**
     * 获取用户信息
     *
     * @return BladeUser
     */
    public static YglUser getUser() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }
        // 优先从 request 中获取
        Object yglUser = request.getAttribute(YGL_USER_REQUEST_ATTR);
        if (yglUser == null) {
            yglUser = getUser(request);
            if (yglUser != null) {
                // 设置到 request 中
                request.setAttribute(YGL_USER_REQUEST_ATTR, yglUser);
            }
        }
        return (YglUser) yglUser;
    }

    /**
     * 获取用户信息
     *
     * @param request request
     * @return BladeUser
     */
    public static YglUser getUser(HttpServletRequest request) {
        String jwtStr = getToken(request);
        if (jwtStr == null) {
            return null;
        }
        return getUser(jwtStr);
    }

    /**
     * 获取用户信息
     *
     * @param jwtStr
     * @return
     */
    public static YglUser getUser(String jwtStr) {
        Claims claims = getClaims(jwtStr);
        if (claims == null) {
            return null;
        }
        return claims2User(claims, jwtStr);
    }

    /**
     * claims 转用户信息
     *
     * @param claims
     * @return
     */
    public static YglUser claims2User(Claims claims, String jwtStr) {
        Long userId = NumberUtil.toLong(StringUtil.toStr(claims.get("userId")));
        String mobile = StringUtil.toStr(claims.get("mobile"));
        String mail = StringUtil.toStr(claims.get("mail"));
        String nickName = StringUtil.toStr(claims.get("nickName"));
        String username = StringUtil.toStr(claims.get("username"));
        YglUser yglUser = new YglUser();
        yglUser.setUserId(userId);
        yglUser.setMail(mail);
        yglUser.setMobile(mobile);
        yglUser.setNickName(nickName);
        yglUser.setUsername(username);
        yglUser.setAccessToken(jwtStr);
        return yglUser;
    }

    /**
     * 获取Claims
     *
     * @param request request
     * @return Claims
     */
    public static String getToken(HttpServletRequest request) {
        String auth = request.getHeader(SecureUtil.HEADER);
        if (StringUtils.isNotBlank(auth)) {
            return auth;
        } else {
            String parameter = request.getParameter(SecureUtil.HEADER);
            if (StringUtils.isNotBlank(parameter)) {
                return parameter;
            }
        }
        return null;
    }

    /**
     * 获取Claims
     *
     * @param jwtStr
     * @return
     */
    public static Claims getClaims(String jwtStr) {
        return SecureUtil.parseJwt(jwtStr);
    }

    /**
     * 解析jsonWebToken
     *
     * @param jsonWebToken jsonWebToken
     * @return Claims
     */
    public static Claims parseJwt(String jsonWebToken) {
        try {
            return Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY))
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception ex) {
            return null;
        }
    }

}

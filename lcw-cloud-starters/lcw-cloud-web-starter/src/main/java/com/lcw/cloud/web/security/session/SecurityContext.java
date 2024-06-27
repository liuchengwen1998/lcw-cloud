package com.lcw.cloud.web.security.session;

/**
 * @author yzhang
 * @since 2022/8/20
 * 安全上下文
 */
public class SecurityContext {

    private static final ThreadLocal<Authentication> LOCAL = new ThreadLocal<>();

    /**
     * 获取会话
     *
     * @return
     */
    public static Authentication getAuthentication() {
        return LOCAL.get();
    }

    /**
     * 设置会话
     *
     * @param authentication
     */
    public static void setAuthentication(Authentication authentication) {
        LOCAL.set(authentication);
    }

    /**
     * 清除会话
     */
    public static void clearAuthentication() {
        LOCAL.remove();
    }
}

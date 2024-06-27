package com.lcw.cloud.web.security.session;

/**
 * @author yzhang
 * @since 2022/8/9
 * 会话存储
 */
public interface ISessionStore {

    /**
     * 保存会话信息
     *
     * @param authentication
     * @return
     */
    Boolean save(Authentication authentication);

    /**
     * 刷新会话信息
     *
     * @param sessionId
     */
    void refresh(Long sessionId, String subSystemCode);

    /**
     * 获取会话信息
     *
     * @param sessionId
     * @return
     */
    Authentication get(Long sessionId,String subSystemCode);

    /**
     * 移除会话信息
     *
     * @param sessionId
     */
    void remove(Long sessionId, String subSystemCode);
}

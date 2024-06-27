package com.lcw.cloud.web.security.intercept;

import com.lcw.cloud.web.security.session.Authentication;

/**
 * @author yzhang
 * @since 2022/8/20
 * 判断接口是否有权限访问接口类
 */
public interface IAccessDecisionManager {


    /**
     * 判断当前用户是否具有资源的访问权限
     * @param authentication 当前用户的登录信息
     * @param obj 对应参数，如URL 请求是Request对象，{@link javax.servlet.http.HttpServletRequest}
     */
    void decide(Authentication authentication, Object obj);
}

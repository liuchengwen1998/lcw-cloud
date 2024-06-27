package com.lcw.cloud.web.base;


import com.lcw.cloud.core.base.ErrorMap;
import com.lcw.cloud.core.rest.entity.Result;
import com.lcw.cloud.web.security.session.Authentication;
import com.lcw.cloud.web.security.session.SecurityContext;

import java.util.Objects;

import static com.lcw.cloud.core.base.ErrorMap.ERROR_SESSION_OVERDUE;

/**
 * 基础Controller
 *
 * @author ：yzhang
 * @since ：2022/2/15 09:42
 */
public class BaseController {

    /**
     * 模板方法，简化输出代码编写
     *
     * @param data 结果对象
     * @param <T>  返回结果对象的类型
     * @return
     */
    protected <T> Result<T> success(T data) {
        return Result.success(data);
    }


    /**
     * 获取当前登录会话信息
     *
     * @return
     */
    protected Authentication getAuthentication() {
        Authentication authentication = SecurityContext.getAuthentication();
        if (Objects.isNull(authentication)) {
            throw ErrorMap.ofException(ERROR_SESSION_OVERDUE);
        }
        return authentication;
    }

    /**
     * 获取当前用户编号
     * @return
     */
    protected Long userId() {
        return getAuthentication().getUserId();
    }

}

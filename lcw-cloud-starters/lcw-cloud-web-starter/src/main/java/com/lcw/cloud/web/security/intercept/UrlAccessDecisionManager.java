package com.lcw.cloud.web.security.intercept;

import com.lcw.cloud.core.exception.BizException;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.web.constant.SecurityConstant;
import com.lcw.cloud.web.security.session.Authentication;
import com.lcw.cloud.web.util.RequestUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yzhang
 * @since 2022/8/20
 * URL 方式进行拦截，并进行校验
 */
public class UrlAccessDecisionManager implements IAccessDecisionManager {


    /**
     * 通过URL方式判断是否具有访问权限
     *
     * @param authentication 当前用户的登录信息
     * @param obj            对应参数，如URL 请求是Request对象，{@link HttpServletRequest}
     */
    @Override
    public void decide(Authentication authentication, Object obj) {
        if (vote(authentication, RequestUtil.getRequestPath((HttpServletRequest) obj))) {
            return;
        }
        throw new BizException(HttpStatus.HTTP_STATUS_401.getCode(), HttpStatus.HTTP_STATUS_401.getMessage());
    }

    /**
     * 判断资源是否有访问权限
     *
     * @param authentication 当前登录用户
     * @param requestPath    真实请求地址
     * @return 有访问权限返回True，没有返回False
     */
    private boolean vote(Authentication authentication, String requestPath) {
        // 获取身份信息
        List<String> perms = authentication.getAuthority().get(SecurityConstant.PERM_TYPE_FUNCTION);
        if (Objects.isNull(perms)) {
            return Boolean.FALSE;
        }
        for (String perm : perms) {
            if (StringUtils.equalsIgnoreCase(SecurityConstant.PERM_ALL, perm) ||
                    RequestUtil.antMatch(removePrefix(perm), requestPath)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public static String removePrefix(String perm) {
        if (perm.contains("/")) {
            Pattern compile = Pattern.compile("/");
            Matcher m = compile.matcher(perm);
            boolean ack = false;
            while (m.find()) {
                if (ack) {
                    return perm.substring(m.end()-1);
                } else {
                    ack = true;
                }
            }
        }
        return perm;
    }

}

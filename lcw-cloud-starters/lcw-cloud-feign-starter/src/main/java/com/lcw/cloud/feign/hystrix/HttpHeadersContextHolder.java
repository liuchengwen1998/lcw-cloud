package com.lcw.cloud.feign.hystrix;

import com.lcw.cloud.core.constant.CommonConstants;
import com.lcw.cloud.feign.util.WebUtil;
import com.lcw.cloud.web.security.session.Authentication;
import com.lcw.cloud.web.security.session.SecurityContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.NamedThreadLocal;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * HttpHeadersContext
 *
 * @author L.cm
 */
public class HttpHeadersContextHolder {
    private static final ThreadLocal<HttpHeaders> HTTP_HEADERS_HOLDER = new NamedThreadLocal<>("hystrix HttpHeaders");

    /**
     * 请求和转发的ip
     */
    private static final String[] ALLOW_HEADS = new String[]{
            "X-Real-IP", "x-forwarded-for", "User-Agent"
    };

    static void set(HttpHeaders httpHeaders) {
        HTTP_HEADERS_HOLDER.set(httpHeaders);
    }

    @Nullable
    public static HttpHeaders get() {
        return HTTP_HEADERS_HOLDER.get();
    }

    static void remove() {
        HTTP_HEADERS_HOLDER.remove();
    }

    @Nullable
    public static HttpHeaders toHeaders() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }
        HttpHeaders headers = new HttpHeaders();
        String accountHeaderName = CommonConstants.ACCESS_TOKEN;
        // 如果当前有token信息，继续往下传递
        Authentication auth = SecurityContext.getAuthentication();
        if (auth != null) {
            headers.add(CommonConstants.TOKEN, auth.getAccessToken());
        }

        List<String> allowHeadsList = new ArrayList<>(Arrays.asList(ALLOW_HEADS));
        // 如果有传递 account header 继续往下层传递
        allowHeadsList.add(accountHeaderName);
        // 传递请求头
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                // 只支持配置的 header
                if (allowHeadsList.contains(key)) {
                    String values = request.getHeader(key);
                    // header value 不为空的 传递
                    if (StringUtils.isNotBlank(values)) {
                        headers.add(key, values);
                    }
                }

            }
        }
        return headers.isEmpty() ? null : headers;
    }
}

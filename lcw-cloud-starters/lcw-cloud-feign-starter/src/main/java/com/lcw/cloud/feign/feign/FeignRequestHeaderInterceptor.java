package com.lcw.cloud.feign.feign;

import com.lcw.cloud.core.constant.CommonConstants;
import com.lcw.cloud.feign.hystrix.HttpHeadersContextHolder;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

/**
 * feign 传递Request header
 *
 * @author L.cm
 */
@NoArgsConstructor
@AllArgsConstructor
public class FeignRequestHeaderInterceptor implements RequestInterceptor {

    private String appName;

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpHeaders headers = HttpHeadersContextHolder.get();
        requestTemplate.header(CommonConstants.APP_HEADER, CommonConstants.APP_HEADER_TOKEN);
        requestTemplate.header(CommonConstants.APP_NAME,appName);
        if (headers != null && !headers.isEmpty()) {
            headers.forEach((key, values) -> values.forEach(value -> requestTemplate.header(key, value)));
        }
    }

}

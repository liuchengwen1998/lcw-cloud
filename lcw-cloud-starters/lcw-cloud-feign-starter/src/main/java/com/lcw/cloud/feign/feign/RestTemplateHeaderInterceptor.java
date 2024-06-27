package com.lcw.cloud.feign.feign;

import com.lcw.cloud.core.constant.CommonConstants;
import com.lcw.cloud.feign.hystrix.HttpHeadersContextHolder;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * RestTemplateHeaderInterceptor 传递Request header
 *
 * @author L.cm
 */
@AllArgsConstructor
public class RestTemplateHeaderInterceptor implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(
            HttpRequest request, byte[] bytes,
            ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = HttpHeadersContextHolder.get();
        // 考虑2中情况 1. RestTemplate 不是用 hystrix 2. 使用 hystrix
        if (headers == null) {
            headers = HttpHeadersContextHolder.toHeaders();
        }
        HttpHeaders httpHeaders = request.getHeaders();
        if (null!=headers&&!headers.isEmpty()) {
            headers.add(CommonConstants.APP_HEADER, CommonConstants.APP_HEADER_TOKEN);
            headers.forEach((key, values) -> {
                values.forEach(value -> httpHeaders.add(key, value));
            });
        }
        return execution.execute(request, bytes);
    }
}

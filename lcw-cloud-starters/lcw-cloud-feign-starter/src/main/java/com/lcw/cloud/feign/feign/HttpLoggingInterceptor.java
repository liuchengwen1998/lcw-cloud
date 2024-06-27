package com.lcw.cloud.feign.feign;

import com.lcw.cloud.core.exception.BizException;
import com.lcw.cloud.core.logger.LogFactory;
import okhttp3.*;
import okio.Buffer;

import java.io.IOException;

/**
 * @author yzhang
 * @since 2022/8/8
 */
public class HttpLoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        // 打印请求日志
        logForRequest(request);
        Response response;
        try {
            response = chain.proceed(request);
            ResponseBody body = response.body();
            if (body != null) {
                MediaType mediaType = body.contentType();
                if (mediaType != null && isText(mediaType)) {
                    String content = body.string();
                    LogFactory.bizTraceLog("请求的url为{},调用返回结果：contentType:{},content is:{},", request.url().toString(), mediaType, content);
                    body = ResponseBody.create(mediaType, content);
                    return response.newBuilder().body(body).build();
                }
            }
        } catch (Exception e) {
            if (e instanceof BizException) {
                throw e;
            }
            LogFactory.err("<-- HTTP FAILED: " + request.url(), e);
            throw e;
        }
        return response;
    }

    private boolean isText(MediaType mediaType) {
        if ("text".equals(mediaType.type())) {
            return true;
        }
        if ("json".equals(mediaType.subtype()) ||
                "xml".equals(mediaType.subtype()) ||
                "html".equals(mediaType.subtype()) ||
                "webviewhtml".equals(mediaType.subtype())) {
            return true;
        }
        return false;
    }


    /**
     * 打印请求日志
     *
     * @param request
     */
    private void logForRequest(Request request) {
        String url = request.url().toString();
        String method = request.method();
        Headers headers = request.headers();
        String headerStr = headers.size() > 0 ? headers.toString() : "";
        LogFactory.bizTraceLog("请求路径为url:{},method:{},headers:{}", url, method, headerStr);
        RequestBody requestBody = request.body();
        if (requestBody != null) {
            MediaType mediaType = requestBody.contentType();
            if (mediaType != null && isText(mediaType)) {
                LogFactory.bizTraceLog("请求的路径为{} mediaType:{},bodyToString:{}", url, mediaType.toString(), bodyToString(request));
            }
        }
    }

    private String bodyToString(final Request request) {
        final Request copy = request.newBuilder().build();
        final Buffer buffer = new Buffer();
        try {
            copy.body().writeTo(buffer);
        } catch (IOException e) {
            return "something error,when show requestBody";
        }
        return buffer.readUtf8();
    }

}

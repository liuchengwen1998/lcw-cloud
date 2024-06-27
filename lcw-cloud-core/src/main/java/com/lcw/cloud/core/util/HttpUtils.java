package com.lcw.cloud.core.util;

import com.alibaba.fastjson.JSONObject;
import com.lcw.cloud.core.logger.LogFactory;
import okhttp3.*;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author yzhang
 * @since 2022-07-27
 */
public class HttpUtils {

    /**
     * GET REQUEST
     *
     * @param url    请求地址
     * @param params 请求参数，按照a=b的形式拼接的参数
     * @return 请求结果 字符串
     */
    public static String get(String url, String params) {
        return get(url, new HashMap<>(), params);
    }

    /**
     * 获取get请求，添加有header
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String get(String url, Map<String, String> headers, String params) {
        LogFactory.bizTraceLog("第三方请求路径为{},请求的头部信息为{},请求的参数为{}", url, headers, params);
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient client = new OkHttpClient().newBuilder().sslSocketFactory(sslSocketFactory,
                            (X509TrustManager) trustAllCerts[0]).readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS).build();
            Headers.Builder headerBuilder = new Headers.Builder();
            headers.forEach(headerBuilder::add);
            Request request = new Request.Builder().url(url + "?" + params).get().headers(headerBuilder.build()).build();
            Response response = client.newCall(request).execute();
            if (Objects.nonNull(response.body())) {
                String content = response.body().string();
                LogFactory.bizTraceLog("第三方请求路径为{},返回数据为{}", url, content);
                return content;
            }
            LogFactory.bizError("接口请求失败，请求的接口为{}", url);
            return "";
        } catch (IOException | NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
            return "";
        }
    }


    /**
     * GET REQUEST
     *
     * @param url    请求地址
     * @param params 请求参数，map集合
     * @return 请求返回字符串
     */
    public static String get(String url, Map<String, Object> params) {
        return get(url, mapToRequestString(params));
    }

    /**
     * get请求添加header
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String get(String url, Map<String, String> headers, Map<String, Object> params) {
        return get(url, headers, mapToRequestString(params));
    }

    /**
     * GET REQUEST
     *
     * @param url    请求地址
     * @param params 请求参数，map集合
     * @param clazz  返回对象类型
     * @param <T>    返回对象类型
     * @return 根据传入的类型返回出解析后的对象
     */
    public static <T> T get(String url, Map<String, Object> params, Class<T> clazz) {
        return JSONObject.parseObject(get(url, mapToRequestString(params)), clazz);
    }

    /**
     * POST REQUEST
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return 返回请求结果字符串
     * @throws IOException 异常处理
     */
    public static String postForm(String url, Map<String, String> params) {
        return postForm(url, new HashMap<>(), params);
    }

    /**
     * postform请求携带headers
     *
     * @param url
     * @param headers
     * @param params
     * @return
     */
    public static String postForm(String url, Map<String, String> headers, Map<String, String> params) {
        LogFactory.bizTraceLog("第三方请求路径为{},请求的头部信息为{},请求的参数为{}", url, headers, params);
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient client = new OkHttpClient().newBuilder().sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]).build();
            MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (String key : params.keySet()) {
                requestBodyBuilder.addFormDataPart(key, params.get(key));
            }
            Headers.Builder headerBuilder = new Headers.Builder();
            headers.forEach(headerBuilder::add);
            RequestBody body = requestBodyBuilder.build();
            Request request = new Request.Builder().url(url).method("POST", body).headers(headerBuilder.build()).build();
            Response response = client.newCall(request).execute();
            if (Objects.nonNull(response.body())) {
                String content = response.body().string();
                LogFactory.bizTraceLog("第三方请求路径为{},返回数据为{}", url, content);
                return content;
            }
            LogFactory.bizError("接口请求失败，请求的接口为{}", url);
            return "";
        } catch (Exception ex) {
            LogFactory.bizErr("http请求失败，失败原因为" + ex.getMessage(), ex);
            throw new RuntimeException("http post form 请求发生异常");
        }
    }


    /**
     * POST REQUEST FORM
     *
     * @param url    请求地址
     * @param params 请求参数map集合
     * @param clazz  返回对象类型
     * @param <T>    返回对象类型
     * @return 返回传入的对象类型，实际就是将返回结果用fastJson 转成对象
     */
    public static <T> T postForm(String url, Map<String, String> params, Class<T> clazz) {
        return JSONObject.parseObject(postForm(url, params), clazz);
    }

    /**
     * POST REQUEST JSON
     *
     * @param url    请求url
     * @param params 请求参数
     * @return 返回结果字符串
     * @throws IOException          异常冒泡
     * @throws NullPointerException 异常冒泡
     */
    public static String postJSON(String url, Map<String, Object> params) {
        return postJSON(url, new HashMap<>(), params);
    }

    /**
     * POST REQUEST JSON
     *
     * @param url          请求url
     * @param headerParams 头部请求参数
     * @param params       请求参数
     * @return 返回结果字符串
     * @throws IOException          异常冒泡
     * @throws NullPointerException 异常冒泡
     */
    public static String postJSON(String url, Map<String, String> headerParams, Map<String, Object> params) {
        LogFactory.bizTraceLog("第三方请求路径为{},请求的头部信息为{},请求的参数为{}", url, headerParams, params);
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[]{};
                }
            }};
            final SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient client = new OkHttpClient().newBuilder().sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .readTimeout(30, TimeUnit.SECONDS).writeTimeout(30, TimeUnit.SECONDS).connectTimeout(30, TimeUnit.SECONDS).build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, JSONObject.toJSONString(params));
            Headers.Builder headerBuilder = new Headers.Builder();
            headerParams.forEach(headerBuilder::add);
            headerBuilder.add("Content-Type", "application/json");
            Request request = new Request.Builder().url(url).method("POST", body).headers(headerBuilder.build()).build();
            Response response = client.newCall(request).execute();
            if (Objects.nonNull(response.body())) {
                String content = response.body().string();
                LogFactory.bizTraceLog("第三方请求路径为{},返回的值为{}", url, content);
                return content;
            }
            LogFactory.bizError("接口请求失败，请求的接口为{}", url);
            return "";
        } catch (Exception ex) {
            LogFactory.bizErr("http请求失败，失败原因为" + ex.getMessage(), ex);
            throw new RuntimeException("http post 请求发生异常,异常原因为:" + ex.getMessage());
        }

    }

    /**
     * POST REQUEST JSON
     *
     * @param url    请求URL
     * @param params 请求参数
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T postJSON(String url, Map<String, Object> params, Class<T> clazz) {
        return JSONObject.parseObject(postJSON(url, params), clazz);
    }

    /**
     * 格式化MAP参数
     *
     * @param paramMap
     * @return
     */
    private static String mapToRequestString(Map<String, Object> paramMap) {
        StringBuilder paramString = new StringBuilder();
        Iterator<String> keys = paramMap.keySet().iterator();
        while (keys.hasNext()) {
            String key = keys.next();
            paramString.append(key);
            paramString.append("=");
            paramString.append(paramMap.get(key));
            if (keys.hasNext()) {
                paramString.append("&");
            }
        }
        return paramString.toString();
    }

}

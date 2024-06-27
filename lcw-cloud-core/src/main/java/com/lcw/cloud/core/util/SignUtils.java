package com.lcw.cloud.core.util;

import com.alibaba.fastjson.JSONObject;
import com.lcw.cloud.core.logger.LogFactory;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @author yzhang
 * @since 2022/10/12
 * 参数签名工具类，可以对请求参数进行签名和验签操作
 * <p>
 * HTTP请求方法\n
 * URL\n
 * 请求时间戳\n
 * 请求随机串\n
 * 请求报文主体\n
 * <p>
 * 签名的时候将 请求时间戳、HTTP请求方法和请求参数都放到map里面进行签名，key为请求随机串，signField为sign
 */
public class SignUtils {

    // 请求方法
    public static final String REQUEST_METHOD = "request_method";

    // 请求路径
    public static final String REQUEST_PATH = "request_path";

    // 时间戳
    private static final String REQUEST_TIMESTAMP = "timestamp";

    // 签名字段
    public static final String REQUEST_SIGN = "sign";


    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data Map类型数据
     * @param key  API密钥
     * @return 签名是否正确
     * @throws Exception
     */
    public static boolean verify(Map<String, Object> data, String key, String signField) {
        if (!data.containsKey(signField) || !data.containsKey(REQUEST_TIMESTAMP) || StringUtils.isBlank(data.get(REQUEST_TIMESTAMP).toString())) {
            LogFactory.bizError("当前请求接口未包含验签相关数据，需要重新请求，请求的内容为{}", JSONObject.toJSONString(data));
            return false;
        }
        // 校验时间戳，大于5分钟的接口自动失效
        long timestamp = Long.parseLong(data.get(REQUEST_TIMESTAMP).toString());
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(-5);
        boolean compareDate = DateUtils.compareDate(Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant()), new Date(timestamp));
        if (!compareDate) {
            LogFactory.bizError("当前请求接口已经失效，需要重新请求，请求的时间为{},当前时间为{}", timestamp, LocalDateTime.now());
            return false;
        }

        Object sign = data.get(signField);
        return sign(data, key, signField).equals(sign);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data 待签名数据
     * @param key  API密钥
     * @return 签名
     */
    public static String sign(final Map<String, Object> data, String key, String signField) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[0]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(signField)) {
                continue;
            }
            // 参数值为空，则不参与签名
            if (data.get(k).toString().trim().length() > 0) {
                sb.append(k).append("=").append(data.get(k).toString().trim()).append("&");
            }
        }

        sb.append("key=").append(key);
        String result = MD5Utils.md5HexSimple(sb.toString()).toUpperCase();
        LogFactory.biz("生成的签名为{}", result);
        return result;
    }

    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put(REQUEST_METHOD, "GET");
        params.put(REQUEST_PATH, "/get/test");
        params.put(REQUEST_TIMESTAMP, "1665563337");
        params.put("userName", "张三");
        params.put("age", "23");
        params.put(REQUEST_SIGN, "D42D6C1B04D4789FF52DF60FC75A34D9");
        System.out.println(verify(params, "123456", REQUEST_SIGN));
    }


}

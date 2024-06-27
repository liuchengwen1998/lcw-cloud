package com.lcw.cloud.core.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class JWTUtils {

    //过期时间 15分钟
    private static final long EXPIRE_TIME = 15 * 60 * 1000;
    //私钥
    private static final String TOKEN_SECRET = "ygl";
    /**
     * jwt 格式校验
     */
    public static final Pattern JWTPattern = Pattern.compile("^.*(\\..*){2}$");

    private static final ObjectMapper JSON = new ObjectMapper();

    /**
     * 生成签名，15分钟过期
     * 根据内部改造，支持6中类型，Integer,Long,Boolean,Double,String,Date
     *
     * @param map
     * @return
     */
    public static String sign(Map<String, Object> map) {
        return sign(map, EXPIRE_TIME);
    }

    public static String sign(Map<String, Object> map, Long expireTime) {
        //设置过期时间
        Date date = new Date(System.currentTimeMillis() + expireTime);
        return sign(map, date);
    }

    /**
     * 生成签名，15分钟过期
     * 根据内部改造，支持6中类型，Integer,Long,Boolean,Double,String,Date
     *
     * @param map
     * @param expireDate 有效期时间
     * @return
     */
    public static String sign(Map<String, Object> map, Date expireDate) {
        try {
            //私钥和加密算法
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);

            //设置头部信息
            Map<String, Object> header = new HashMap<>(2);
            header.put("typ", "jwt");

            //返回token字符串
            JWTCreator.Builder builder = JWT.create().withHeader(header)
                    .withIssuedAt(new Date()) //发证时间
                    .withExpiresAt(expireDate);  //过期时间

            map.forEach((key, value) -> {
                if (value instanceof Integer) {
                    builder.withClaim(key, (Integer) value);
                } else if (value instanceof Long) {
                    builder.withClaim(key, (Long) value);
                } else if (value instanceof Boolean) {
                    builder.withClaim(key, (Boolean) value);
                } else if (value instanceof String) {
                    builder.withClaim(key, String.valueOf(value));
                } else if (value instanceof Double) {
                    builder.withClaim(key, (Double) value);
                } else if (value instanceof Date) {
                    builder.withClaim(key, (Date) value);
                }
            });
            return builder.sign(algorithm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 检验token是否正确
     *
     * @param **token**
     * @return
     */
    public static boolean verify(String token) {
        // 先校验格式
        if (!JWTPattern.matcher(token).matches()) {
            return false;
        }
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取用户自定义Claim集合
     *
     * @param token
     * @return
     */
    public static Map<String, Claim> getClaims(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token).getClaims();
    }

    /**
     * 获取过期时间
     *
     * @param token
     * @return
     */
    public static Date getExpiresAt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        return JWT.require(algorithm).build().verify(token).getExpiresAt();
    }

    /**
     * 获取jwt发布时间
     */
    public static Date getIssuedAt(String token) {
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        return JWT.require(algorithm).build().verify(token).getIssuedAt();
    }

    /**
     * 验证token是否失效
     *
     * @param token
     * @return true:过期   false:没过期
     */
    public static boolean isExpired(String token) {
        try {
            final Date expiration = getExpiresAt(token);
            return expiration.before(new Date());
        } catch (TokenExpiredException e) {
            return true;
        }
    }

    /**
     * 直接Base64解密获取header内容
     *
     * @param token
     * @return
     */
    public static String getHeaderByBase64(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        } else {
            byte[] header_byte = Base64.getDecoder().decode(token.split("\\.")[0]);
            return new String(header_byte);
        }
    }

    /**
     * 直接Base64解密获取payload内容
     *
     * @param token
     * @return
     */
    public static String getPayloadByBase64(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        } else {
            byte[] payload_byte = Base64.getDecoder().decode(token.split("\\.")[1]);
            return new String(payload_byte);
        }
    }

    /**
     * 直接Base64解密获取payload内容
     *
     * @param token
     * @return
     */
    public static Map<String, Object> getPayloadByBase64Map(String token) {

        String jsonPayload = getPayloadByBase64(token).trim();
        Map<String, Object> res = new HashMap<>();
        try {
            if (!"".equals(jsonPayload)) {
                res = (Map<String, Object>) JSON.readValue(jsonPayload, Map.class);
            }
            return res;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    /**
     * 主函数
     *
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        Map<String, Object> params = new HashMap<>();
        String token = sign(params);
//        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJjdXN0X2NvZGUiOiIxMjM0NTYiLCJzY29wZSI6WyJ3cml0ZSIsInJlYWQiXSwiZXhwIjoxNjI5MTk1MjUwLCJqdGkiOiI5MFZLeWJBS1dQY1hrWV9hV0xzcTlwSVNyM0kiLCJjbGllbnRfaWQiOiJhbmNodSJ9.ozRmPw2lUpaRZL5Yv-NacxrXaGtl-tj3X6GmtsGpbbE";
        System.out.println(verify(token));//验证token是否正确
        System.out.println("获取签发token时间：" + getIssuedAt(token));
        System.out.println("获取过期时间：" + getExpiresAt(token));
        // Thread.sleep(1000*40);
        System.out.println("检查是否已过期：" + isExpired(token));
        System.out.println("获取头" + getHeaderByBase64(token));
        System.out.println("获取负荷" + getPayloadByBase64(token));
    }

}

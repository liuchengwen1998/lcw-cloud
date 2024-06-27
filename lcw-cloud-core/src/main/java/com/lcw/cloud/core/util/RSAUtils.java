package com.lcw.cloud.core.util;

import com.lcw.cloud.core.base.ErrorMap;

import javax.crypto.Cipher;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * RSA工具类
 *
 * @author yzhang
 * @since 2022/07/27
 */
public class RSAUtils {

    public static final String ALGORITHM_RSA = "RSA";

    public static final String ALGORITHM_CIPHER = "RSA/ECB/PKCS1Padding";

    /**
     * 默认随机秘钥
     */
    private static final String SECRET = "9GX4WQx7gD";

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        //base64编码的公钥
        byte[] decoded = decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance(ALGORITHM_RSA).generatePublic(new X509EncodedKeySpec(decoded));
        //RSA加密
        Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        return encodeBase64(cipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * RSA私钥解密
     *
     * @param str        加密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey) throws Exception {
        //64位解码加密后的字符串
        byte[] inputByte = decodeBase64(str);
        //base64编码的私钥
        byte[] decoded = decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance(ALGORITHM_RSA).generatePrivate(new PKCS8EncodedKeySpec(decoded));
        //RSA解密
        Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * 生成键值对
     * @return
     */
    public static RSAKey generateKey() {
        return generateKey(SECRET);
    }
    /**
     * 根据密文，生存rsa公钥和私钥,并返回RSA，KEY
     *
     * @throws Exception 秘钥生成错误
     */
    public static RSAKey generateKey(String secret) {
        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ALGORITHM_RSA);
        } catch (NoSuchAlgorithmException e) {
            throw ErrorMap.ofException(ErrorMap.ERROR_SECRET_NOT_SUPPORT);
        }
        SecureRandom secureRandom = new SecureRandom(secret.getBytes());
        keyPairGenerator.initialize(1024, secureRandom);
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        // 获取公钥并写出
        byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
        // 获取私钥并写出
        byte[] privateKeyBytes = keyPair.getPrivate().getEncoded();
        RSAKey rsaKey = new RSAKey();
        rsaKey.setPublicKey(encodeBase64(publicKeyBytes));
        rsaKey.setPrivateKey(encodeBase64(privateKeyBytes));
        return rsaKey;
    }

    /**
     * 使用Base64 对秘钥进行编码
     *
     * @param binaryData
     * @return
     */
    public static String encodeBase64(byte[] binaryData) {
        Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(binaryData);
    }

    /**
     * 使用Base64 对秘钥进行解码
     *
     * @param encoded
     * @return
     */
    public static byte[] decodeBase64(String encoded) {
        Decoder decoder = Base64.getDecoder();
        return decoder.decode(encoded);
    }

    public static void main(String[] args) throws Exception {
        RSAKey rsaKey = RSAUtils.generateKey();
//        String enStr = RSAUtils.encrypt("1234567", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCcO5pPG6tCcLQwsGK" +
//                "/V6aZBlRdvpAUYhmsB4iMA2WXZaaPUH/LwLSD8nNSRu5IseR4xWvUeD+tNLbMC/wYo8mfJs4+QiStm8NiV7wJ+gRJLHV5/5nBHDyfuEmOW0H1UUwDATXnBgMA7zQknkqH1E1w/4qw+R5+XHdivVquI1WrZwIDAQAB");
//        System.out.println("密文：" + enStr);
//        System.out.println(RSAUtils.decrypt("Co2sUBDQFMrEHqHMtSfEXy3GHlQbQo++YiK3vu9XjvGGK9GI0YVyd6jc3Y9z6EK1IeyUAgfMYFiyY6WxjouNvRhwxIM5GBxrLuyJepvqdrDO3I2t7x78mmyvnf5dnyRvr6Q/b4oCrmcUizI4DVkFT1Ctazz7XwzjMd/z+4Yu9q8=",
//                "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJw7mk8bq0JwtDCwYr9XppkGVF2+kBRiGawHiIwDZZdlpo9Qf8vAtIPyc1JG7kix5HjFa9R4P600tswL/BijyZ8mzj5CJK2bw2JXvAn6BEksdXn/mcEcPJ+4SY5bQfVRTAMBNecGAwDvNCSeSofUTXD/irD5Hn5cd2K9Wq4jVatnAgMBAAECgYAHZueDsfrmNfLDj8fN6vgwrdUXb+zhmlg/B0hX4d1RU/aVOaokXCfxTG5cSpIFjbAWbLwUH/JcYW/7sOGrxS7LMY6/DBClvGWFoYHmw1y7WeSgb6MdAj2Iir1d8WXddTp9VPLEZojjFlJpdX/OVY8mcpZ761WKlMWLqTShzwqUQQJBANQidC299GFUZ4+T1SLbjmHF28desbyu+w5w92H+yQj8i8AYRuzCGOgncwkv5x+bLw/J8mt5JULb5hsBeZWd48cCQQC8ie+b41GBZlICUjt/OqT5Wmwm9C8rPJ13zs38VCtJDoiIQoh+ZKMqoDXrEzigdOp7LXN1Xkj/NrM8K/C54LthAkBH0qZ9Ojw8m+Q4U3gtRiLt7Zyc4u3QkXoFCLBpaGdOeJskXdPdo1XlfD3RlXtuUw9bDCfcxXadLXC6IgS1aHDPAkEAqUr2MzTvt9CHrPvn3rtq7VgrKuMyA5HfF3hD7+IoJ+Z75FZRt6Rn+tt27skInNO+xLIwLWBnDXsxf8M42b8YoQJBAJ3GTQ9V1GGzR9h9lZyP+K60yB4ZbCWO52V/d3iwnCbOQ9xvw6hicnV4ClDIgOc40h24LotdyhQoLGcJE0CKJco="));
        System.out.println("公钥:" + rsaKey.publicKey);
        System.out.println("私钥：" +rsaKey.privateKey);



    }

    public static class RSAKey implements Serializable {

        private static final long serialVersionUID = -1776340498983471665L;
        /**
         * 公钥
         */
        private String publicKey;

        /**
         * 私钥
         */
        private String privateKey;

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }
    }
}

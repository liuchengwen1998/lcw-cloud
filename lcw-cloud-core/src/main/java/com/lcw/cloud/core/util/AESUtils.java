package com.lcw.cloud.core.util;


import com.lcw.cloud.core.base.ErrorMap;
import com.lcw.cloud.core.logger.LogFactory;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES算法工具类
 *
 * @author yzhang
 * @since 2022/07/27
 */
public class AESUtils {
    static final String AES_ALGORITHM = "AES";
    static Charset charset = StandardCharsets.UTF_8;

    /**
     * 生成随机密钥
     *
     * @return 密钥密文
     */
    public static String generateKey() { // 生成密钥
        KeyGenerator secretGenerator = null;
        try {
            secretGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            LogFactory.bizErr("获取秘钥失败，失败的原因为{}", e);
            throw ErrorMap.ofException(ErrorMap.ERROR_SECRET_NOT_SUPPORT);
        }
        SecureRandom secureRandom = new SecureRandom();
        secretGenerator.init(256, secureRandom);
        SecretKey secretKey = secretGenerator.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * AES加密 加密结果进行base64编码
     *
     * @param content 明文
     * @param key     密钥
     * @return base64编码的密文
     */
    public static String encrypt(String content, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException { // 加密
        return encodeBase64(aes(content.getBytes(charset), Cipher.ENCRYPT_MODE, key));
    }

    /**
     * AES解密 对base64编码过的结果进行解密
     *
     * @param content 编码过的密文
     * @param key     密钥
     * @return
     */
    public static String decrypt(String content, String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException { // 解密
        return new String(aes(decodeBase64(content), Cipher.DECRYPT_MODE, key), charset);
    }

    /**
     * 使用Base64 进行编码
     *
     * @param binaryData
     * @return
     */
    public static String encodeBase64(byte[] binaryData) {
        Base64.Encoder encoder = Base64.getEncoder();
        return encoder.encodeToString(binaryData);
    }

    /**
     * 使用Base64 进行解码
     *
     * @param encoded
     * @return
     */
    public static byte[] decodeBase64(String encoded) {
        Base64.Decoder decoder = Base64.getDecoder();
        return decoder.decode(encoded);
    }

    /**
     * @param contentArray 内容数组
     * @param mode         解密或者加密
     * @param secretKey    Base64加密密钥
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    private static byte[] aes(byte[] contentArray, int mode, String secretKey)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        // Base64密码解密
        byte[] encodedKey = Base64.getDecoder().decode(secretKey);
        // 根据给定的字节数组使用AES加密算法构造一个密钥
        SecretKey key = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
        cipher.init(mode, key);
        return cipher.doFinal(contentArray);
    }

    public static void main(String[] args) {
        String content = "{\"userId\":1,\"randomContent\":\"" + RandomUtils.generateString(32) + "\"}";
        String secretKey;
        try {
            long timeStart = System.currentTimeMillis();
            secretKey = generateKey();
            System.out.println(secretKey);
            String encryptResult = encrypt(content, secretKey);
            long timeEnd = System.currentTimeMillis();
            System.out.println("加密后的结果为：" + encryptResult);
            String decryptResult = decrypt(encryptResult, secretKey);
            System.out.println("解密后的结果为：" + decryptResult);
            System.out.println("加解密用时：" + (timeEnd - timeStart));
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException |
                 BadPaddingException e) {
            e.printStackTrace();
        }
    }
}

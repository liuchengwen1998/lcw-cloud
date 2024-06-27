package com.lcw.cloud.core.util;

import java.util.Random;

/**
 * 随机数生成工具类
 *
 * @author yzhang
 * @since 2022/07/27
 */
public class RandomUtils {

    public static final String ALL_CHAR = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /**
     * 返回一个定长的随机字符串(只包含大小写字母、数字)
     *
     * @param length 随机字符串长度
     * @return 随机字符串
     */
    public static String generateString(int length) {
        char[] b = new char[length];
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            b[i] = ALL_CHAR.charAt(random.nextInt(ALL_CHAR.length()));
        }
        return new String(b);
    }

    public static void main(String[] args) {
        Long startTime = System.currentTimeMillis();
        System.out.println("返回一个定长的随机字符串(只包含大小写字母、数字):" + generateString(32));
        Long endTime = System.currentTimeMillis();
        System.out.println("用时:" + (endTime - startTime));
    }
}

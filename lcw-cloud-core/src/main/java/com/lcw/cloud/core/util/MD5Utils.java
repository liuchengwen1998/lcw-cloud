package com.lcw.cloud.core.util;

import com.lcw.cloud.core.base.ErrorMap;
import com.lcw.cloud.core.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

public class MD5Utils {

    /**
     * MD5加密加盐
     *
     * @param value
     * @return
     */
    public static String md5Hex(String value, String salt) {
        String str = DigestUtils.md5DigestAsHex(value.getBytes());
        String sub = str.substring(0, 8);
        return DigestUtils.md5DigestAsHex((sub + salt).getBytes());
    }


    /**
     * 简化的MD5加密
     * @param value
     * @return
     */
    public static String md5HexSimple(String value) {
        return DigestUtils.md5DigestAsHex(value.getBytes());
    }

    /**
     * 校验是否一致
     *
     * @param oldMd5 已经加密过的
     * @param value  比较的值
     * @param salt   加入的盐
     * @return
     */
    public static boolean check(String oldMd5, String value, String salt) {
        Preconditions.checkState(StringUtils.isNoneBlank(oldMd5), ErrorMap.ERROR_DATA_NOT_NULL);
        return oldMd5.equals(md5Hex(value, salt));
    }

    public static void main(String[] args) {
//        System.out.println(MD5Utils.md5Hex("ygl15a", ""));
        System.out.println(DigestUtils.md5DigestAsHex("ygl15a".getBytes()));
        // d7cb3157ff9427c2e8a6cdba6abe63ac
        // d7cb3157ff9427c2e8a6cdba6abe63ac
    }

}

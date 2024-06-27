package com.lcw.cloud.core.util;

import com.lcw.cloud.core.entity.FieldAttr;

import java.util.*;

/**
 * @author yzhang
 * @since 2022/8/18
 */
public class MapperUtil {

    /**
     * 字符串转list
     *
     * @param content
     * @return
     */
    public static List<String> strToListStr(String content) {
        if (Objects.isNull(content)) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        String[] split = content.split(",");
        list.addAll(Arrays.asList(split));
        return list;
    }

    /**
     * 字符串转list
     *
     * @param content
     * @return
     */
    public static List<Long> strToListLong(String content) {
        if (Objects.isNull(content)) {
            return new ArrayList<>();
        }
        List<Long> list = new ArrayList<>();
        String[] split = content.split(",");
        for (String s : split) {
            list.add(Long.valueOf(s));
        }
        return list;
    }

    /**
     * list转字符串,用逗号分割
     *
     * @param list
     * @return
     */
    public static String listStrToStr(List<String> list) {
        if (Objects.isNull(list)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        list.forEach(e -> {
            sb.append(e).append(",");
        });
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }


    /**
     * list转字符串,用逗号分割
     *
     * @param list
     * @return
     */
    public static String listLongToStr(List<Long> list) {
        if (Objects.isNull(list)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        list.forEach(e -> {
            sb.append(e).append(",");
        });
        String str = sb.toString();
        return str.substring(0, str.length() - 1);
    }


    /**
     * list 转 map
     *
     * @param req
     * @return
     */
    public static Map<String, Object> listToMap(List<FieldAttr> req) {
        if (Objects.isNull(req)) {
            return new HashMap<>();
        }
        Map<String, Object> map = new HashMap<>();
        req.forEach(e -> {
            map.put(e.getKey(), e.getVal());
        });
        return map;
    }


}

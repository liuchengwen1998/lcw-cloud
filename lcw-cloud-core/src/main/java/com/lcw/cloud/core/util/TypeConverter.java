package com.lcw.cloud.core.util;

import com.lcw.cloud.core.constant.CommonConstants;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Named;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 类型转换
 *
 * @author yzhang
 * @since 2022/07/27
 */
@Named("TypeConverter")
public final class TypeConverter {

    /**
     * 字符串列表转字符串
     *
     * @param list
     * @return
     */
    @Named("listStringToString")
    public static String listStringToString(List<String> list) {
        if (null != list) {
            StringBuilder sb = new StringBuilder();
            list.forEach(str -> {
                sb.append(str);
                sb.append(CommonConstants.COMMA);
            });
            return sb.length() > 0 ? sb.deleteCharAt(sb.length() - 1).toString() : sb.toString();
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 字符串转字符串列表
     *
     * @param str
     * @return
     */
    @Named("stringToListString")
    public static List<String> stringToListString(String str) {
        if (StringUtils.isBlank(str)) {
            return Collections.EMPTY_LIST;
        } else {
            return Arrays.asList(str.split(CommonConstants.COMMA));
        }
    }
}

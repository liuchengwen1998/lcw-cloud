package com.lcw.cloud.core.validation.desensitization;

import java.util.StringJoiner;

/**
 * 姓名脱敏器
 *
 * @author ：yzhang
 * @date ：2022/3/14 16:35
 * @version:
 */
public class DefaultDesensitizationProvider extends AbstractDesensitizationProvider {

    private static final String replacement = "$1****$2";

    private static final String regex = "(.{%s}).*(.{%s})";

    @Override
    protected String getRegex(String sensitiveInfo) {
        int count = (int) Math.floor(sensitiveInfo.length() / 3);
        return String.format(regex, count, count);
    }

    @Override
    protected String getReplacement(String sensitiveInfo) {
        int count = sensitiveInfo.length() - 2 * (int) Math.floor(sensitiveInfo.length() / 3);
        StringJoiner sj = new StringJoiner("");
        sj.add("$1").add(substituteCharacter(count)).add("$2");
        return sj.toString();
    }
}

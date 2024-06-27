package com.lcw.cloud.core.validation.desensitization;

/**
 * 姓名脱敏器，
 * 隐藏姓名中的第一个字，如为英文等其他语种，也是隐藏第一个字母。如：*大友、*安、*ahn
 *
 * @author ：yzhang
 * @date ：2022/3/14 16:35
 * @version:
 */
public class RealNameDesensitizationProvider extends AbstractDesensitizationProvider {

    private static final String replacement = "*$1";

    private static final String regex = ".{1}(.*)";

    @Override
    protected String getRegex(String sensitiveInfo) {
        return regex;
    }

    @Override
    protected String getReplacement(String sensitiveInfo) {
        return replacement;
    }
}

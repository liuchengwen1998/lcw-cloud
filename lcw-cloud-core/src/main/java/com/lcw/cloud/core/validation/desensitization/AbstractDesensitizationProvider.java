package com.lcw.cloud.core.validation.desensitization;

import java.util.StringJoiner;

/**
 * 脱敏接口类
 *
 * @author ：yzhang
 * @date ：2022/3/14 16:31
 * @version:
 */
public abstract class AbstractDesensitizationProvider {

    /**
     * 脱敏
     *
     * @param sensitiveInfo 铭感信息
     * @return
     */
    public String desensitize(String sensitiveInfo) {
        return sensitiveInfo.replaceAll(getRegex(sensitiveInfo), getReplacement(sensitiveInfo));
    }

    protected abstract String getRegex(String sensitiveInfo);

    protected abstract String getReplacement(String sensitiveInfo);


    protected String substituteCharacter(int count) {
        StringJoiner sj = new StringJoiner("");
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                sj.add("*");
            }
        }
        return sj.toString();
    }
}

package com.lcw.cloud.core.validation.desensitization;

import java.util.StringJoiner;

/**
 * 身份证脱敏提供者
 * 显示前1位和后1位，其它用*号代替。（凡是页面显示后无需用户检查确认的，使用这个规则。）
 * 示例：3****************X
 *
 * @author ：yzhang
 * @date ：2022/3/14 16:45
 * @version:
 */
public class BankCardNoDesensitizationProvider extends AbstractDesensitizationProvider {

    private static final String regex = "(\\d{6})\\d*(\\d{4})";

    @Override
    protected String getReplacement(String sensitiveInfo) {
        StringJoiner sj = new StringJoiner("");
        sj.add("$1").add(substituteCharacter(sensitiveInfo.length() - 10))
                .add("$2");
        return sj.toString();
    }

    @Override
    protected String getRegex(String sensitiveInfo) {
        return regex;
    }
}

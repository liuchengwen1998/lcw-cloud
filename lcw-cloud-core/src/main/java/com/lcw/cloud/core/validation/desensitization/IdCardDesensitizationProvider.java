package com.lcw.cloud.core.validation.desensitization;

/**
 * 身份证脱敏提供者
 *显示前1位和后1位，其它用*号代替。（凡是页面显示后无需用户检查确认的，使用这个规则。）
 * 示例：3****************X
 * @author ：yzhang
 * @date ：2022/3/14 16:45
 * @version:
 */
public class IdCardDesensitizationProvider extends AbstractDesensitizationProvider {

  private static final String replacement = "$1****************$2";

  private static final String regex = "(.{1}).*(.{1})";

  @Override
  protected String getReplacement(String sensitiveInfo) {
    return replacement;
  }

  @Override
  protected String getRegex(String sensitiveInfo) {
    return regex;
  }
}

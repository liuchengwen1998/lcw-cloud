package com.lcw.cloud.core.validation.desensitization;

/**
 * 手机号脱敏器
 *
 * @author ：yzhang
 * @date ：2022/3/14 16:53
 * @version:
 */
public class MobileDesensitizationProvider extends AbstractDesensitizationProvider {

  private static final String regex = "(.{3}).*(.{4})";

  private static final String replacement = "$1****$2";


  @Override
  protected String getRegex(String sensitiveInfo) {
    return regex;
  }

  @Override
  protected String getReplacement(String sensitiveInfo) {
    return replacement;
  }
}

package com.lcw.cloud.core.validation.desensitization;

import org.apache.commons.lang3.StringUtils;

/**
 * 手机号脱敏器
 *
 * @author ：yzhang
 * @date ：2022/3/14 16:53
 * @version:
 */
public class TelDesensitizationProvider extends AbstractDesensitizationProvider {

  private static final String regex = "(.*)-\\d*(.{4})";

  @Override
  protected String getRegex(String sensitiveInfo) {
    return regex;
  }

  @Override
  protected String getReplacement(String sensitiveInfo) {
    StringBuilder replacement = new StringBuilder();
    replacement.append("$1").append(calculate(sensitiveInfo)).append("$2");
    return replacement.toString();
  }

  /**
   * 计算需要替换的*号
   * @param sensitiveInfo 原始字符串
   * @return
   */
  private String calculate(String sensitiveInfo) {
    int count =  StringUtils.substringAfter(sensitiveInfo,"-").length()-3;
    return substituteCharacter(count);
  }
}

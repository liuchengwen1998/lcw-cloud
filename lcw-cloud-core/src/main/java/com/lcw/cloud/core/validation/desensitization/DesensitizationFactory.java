package com.lcw.cloud.core.validation.desensitization;

import java.util.HashMap;
import java.util.Map;

/**
 * 敏感信息脱敏工厂类
 *
 * @author ：yzhang
 * @date ：2022/3/14 16:30
 * @version:
 */
public class DesensitizationFactory {

  static final Map<String, AbstractDesensitizationProvider> providers= new HashMap<>();

  static final DefaultDesensitizationProvider DEFAULT_PROVIDER = new DefaultDesensitizationProvider();

  static {
    providers.put(SensitiveInfoType.REAL_NAME, new RealNameDesensitizationProvider());
    providers.put(SensitiveInfoType.ID_CARD, new IdCardDesensitizationProvider());
    providers.put(SensitiveInfoType.MOBILE, new MobileDesensitizationProvider());
    providers.put(SensitiveInfoType.TEL, new TelDesensitizationProvider());
    providers.put(SensitiveInfoType.EMAIL, new EmailDesensitizationProvider());
    providers.put(SensitiveInfoType.BANK_CARD_NO, new BankCardNoDesensitizationProvider());
  }

  public static AbstractDesensitizationProvider getProvider(String sensitiveType) {
    return providers.getOrDefault(sensitiveType,DEFAULT_PROVIDER);
  }

}

package com.lcw.cloud.core.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lcw.cloud.core.base.ErrorMap;
import com.lcw.cloud.core.logger.LogFactory;

/**
 * Json工具类
 *
 * @author ：yzhang
 * @since ：2022/4/19 14:12
 * 
 */
public class JsonUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

  public static  <T> T parse(String json, Class<T> clazz) {
    try {
      return mapper.readValue(json,clazz);
    } catch (JsonProcessingException e) {
      LogFactory.err(e.getMessage(),e);
      throw ErrorMap.ofException(ErrorMap.ERROR_JSON_PARSER_ERROR);
    }
  }

  public static String toJSONString(Object object) {
    try {
      return mapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      LogFactory.err(e.getMessage(),e);
      throw ErrorMap.ofException(ErrorMap.ERROR_JSON_PARSER_ERROR);
    }
  }

}

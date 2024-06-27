package com.lcw.cloud.core.validation;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.lcw.cloud.core.validation.desensitization.SensitiveInfoType;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Jackson脱敏序列化器
 *
 * @author yzhang
 * @since 1.0
 */
@Retention(RetentionPolicy.RUNTIME)
@JacksonAnnotationsInside
@JsonSerialize(using = DesensitizationSerialize.class)
public @interface DesensitizationSerializer {

    /**
     * 敏感信息类型
     *
     * @return
     */
    String type() default SensitiveInfoType.OTHER;

}

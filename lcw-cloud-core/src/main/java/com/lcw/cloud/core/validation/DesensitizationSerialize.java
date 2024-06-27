package com.lcw.cloud.core.validation;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.lcw.cloud.core.validation.desensitization.DesensitizationFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Objects;

/**
 * 对敏感信息进行脱敏处理
 *
 * @author yzhang
 * @since 1.0
 */
public class DesensitizationSerialize extends JsonSerializer<String> implements ContextualSerializer {

    /**
     * 敏感信息类型
     *
     * @return
     */
    private String type;


    public DesensitizationSerialize(String type) {
        this.type = type;
    }

    public DesensitizationSerialize() {
    }

    /**
     * 数字隐藏。
     *
     * @param s
     * @param jsonGenerator
     * @param serializerProvider
     * @throws IOException
     */
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (StringUtils.isBlank(s)) {
            jsonGenerator.writeString("");
        } else {
            jsonGenerator.writeString(DesensitizationFactory.getProvider(this.type).desensitize(s));
        }
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) { // 为空直接跳过
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) { // 非 String 类直接跳过
                DesensitizationSerializer serializer = beanProperty.getAnnotation(DesensitizationSerializer.class);
                if (serializer == null) {
                    serializer = beanProperty.getContextAnnotation(DesensitizationSerializer.class);
                }
                if (serializer != null) { // 如果能得到注解，就将注解的 value 传入 ImageURLSerialize
                    return new DesensitizationSerialize(serializer.type());
                }
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);

    }
}

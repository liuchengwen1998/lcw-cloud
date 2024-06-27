package com.lcw.cloud.core.validation;

import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * 使用正则表达式验证时，支持空字符串""
 *
 * @author yzhang
 * @since 2022/07/27
 */
public class EmptyPatternValidator implements ConstraintValidator<EmptyPattern, String> {

    private EmptyPattern pattern;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 为空"" 和 null 返回true, 符合正则返回true
        return StringUtils.isEmpty(value) ? Boolean.TRUE : Pattern.compile(pattern.regexp()).matcher(value).matches();
    }

    @Override
    public void initialize(EmptyPattern constraintAnnotation) {
        this.pattern = constraintAnnotation;
    }
}

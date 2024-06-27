package com.lcw.cloud.core.base;


import com.lcw.cloud.core.dict.IEnum;
import com.lcw.cloud.core.exception.BizException;

/**
 * 条件判断辅助类,参考 实现，支持异常传入
 *
 * @author yzhang
 * @since 2022/7/27 09:55
 */
public final class Preconditions {

    private Preconditions() {
    }

    /**
     * 校验状态，异常码从map里面取
     *
     * @param expression
     * @param code       错误码
     */
    public static void checkState(boolean expression, String code) {
        if (!expression) {
            throw new BizException(code, ErrorMap.getMsg(code));
        }
    }

    /**
     * 检查状态，如果不符合的时候，异常码从枚举里面获取
     *
     * @param expression
     * @param iEnum
     */
    public static void checkState(boolean expression, IEnum iEnum) {
        if (!expression) {
            throw new BizException(iEnum.getCode(), iEnum.getMsg());
        }
    }

    /**
     * 判断状态是否正确
     *
     * @param expression       判断表达式
     * @param code             错误码
     * @param errorMessageArgs 参数
     */
    public static void checkState(boolean expression, String code, Object... errorMessageArgs) {
        if (!expression) {
            throw new BizException(code, format(ErrorMap.getMsg(code), errorMessageArgs));
        }
    }

    /**
     * 判断状态是否正确
     *
     * @param expression       判断表达式
     * @param iEnum            异常枚举
     * @param errorMessageArgs 参数
     */
    public static void checkState(boolean expression, IEnum iEnum, Object... errorMessageArgs) {
        if (!expression) {
            throw new BizException(iEnum.getCode(), format(iEnum.getMsg(), errorMessageArgs));
        }
    }

    /**
     * 检查参数是否正确
     *
     * @param expression 判断表达式
     * @param code       错误码
     */
    public static void checkArgument(boolean expression, String code) {
        if (!expression) {
            throw new BizException(code, ErrorMap.getMsg(code));
        }
    }

    /**
     * 检查参数是否正确
     *
     * @param expression 判断表达式
     * @param iEnum      错误码
     */
    public static void checkArgument(boolean expression, IEnum iEnum) {
        if (!expression) {
            throw new BizException(iEnum.getCode(), iEnum.getMsg());
        }
    }

    /**
     * 检查参数参数是否正确，支持变量参数
     *
     * @param expression       表达式
     * @param code             错误代码
     * @param errorMessageArgs 替换值
     */
    public static void checkArgument(boolean expression, String code, Object... errorMessageArgs) {
        if (!expression) {
            throw new BizException(code, format(ErrorMap.getMsg(code), errorMessageArgs));
        }
    }

    /**
     * 检查参数参数是否正确，支持变量参数
     *
     * @param expression       表达式
     * @param iEnum            错误代码
     * @param errorMessageArgs 替换值
     */
    public static void checkArgument(boolean expression, IEnum iEnum, Object... errorMessageArgs) {
        if (!expression) {
            throw new BizException(iEnum.getCode(), format(iEnum.getMsg(), errorMessageArgs));
        }
    }

    /**
     * 检查参数参数是否正确，支持变量参数
     *
     * @param reference        对象
     * @param code             错误代码
     * @param errorMessageArgs 替换值
     */
    public static <T> T checkNotNull(T reference, String code, Object... errorMessageArgs) {
        if (reference == null) {
            throw new BizException(code, format(ErrorMap.getMsg(code), errorMessageArgs));
        }
        return reference;
    }

    /**
     * 检查参数参数是否正确，支持变量参数
     *
     * @param reference        对象
     * @param iEnum            错误代码
     * @param errorMessageArgs 替换值
     */
    public static <T> T checkNotNull(T reference, IEnum iEnum, Object... errorMessageArgs) {
        if (reference == null) {
            throw new BizException(iEnum.getCode(), format(iEnum.getMsg(), errorMessageArgs));
        }
        return reference;
    }

    /**
     * @param reference 对象
     * @param code      异常枚举类
     */
    public static <T> T checkNotNull(T reference, String code) {
        if (reference == null) {
            throw new BizException(code, ErrorMap.getMsg(code));
        }
        return reference;
    }

    /**
     * 让其支持 Ienum 参数对象
     *
     * @param reference 对象
     * @param iEnum     异常枚举类
     */
    public static <T> T checkNotNull(T reference, IEnum iEnum) {
        if (reference == null) {
            throw new BizException(iEnum.getCode(), iEnum.getMsg());
        }
        return reference;
    }


    /**
     * Substitutes each {@code %s} in {@code template} with an argument. These are matched by
     * position: the first {@code %s} gets {@code args[0]}, etc.  If there are more arguments than
     * placeholders, the unmatched arguments will be appended to the end of the formatted message in
     * square braces.
     *
     * @param template a non-null string containing 0 or more {@code %s} placeholders.
     * @param args     the arguments to be substituted into the message template. Arguments are converted
     *                 to strings using {@link String#valueOf(Object)}. Arguments can be null.
     */
    // Note that this is somewhat-improperly used from Verify.java as well.
   public static String format(String template, Object... args) {
        template = String.valueOf(template); // null -> "null"

        // start substituting the arguments into the '%s' placeholders
        StringBuilder builder = new StringBuilder(template.length() + 16 * args.length);
        int templateStart = 0;
        int i = 0;
        while (i < args.length) {
            int placeholderStart = template.indexOf("%s", templateStart);
            if (placeholderStart == -1) {
                break;
            }
            builder.append(template, templateStart, placeholderStart);
            builder.append(args[i++]);
            templateStart = placeholderStart + 2;
        }
        builder.append(template.substring(templateStart));

        // if we run out of placeholders, append the extra args in square braces
        if (i < args.length) {
            builder.append(" [");
            builder.append(args[i++]);
            while (i < args.length) {
                builder.append(", ");
                builder.append(args[i++]);
            }
            builder.append(']');
        }

        return builder.toString();
    }
}

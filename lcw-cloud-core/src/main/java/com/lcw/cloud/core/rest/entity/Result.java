package com.lcw.cloud.core.rest.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static com.lcw.cloud.core.rest.entity.HttpStatus.HTTP_STATUS_200;

/**
 * 统一结果返回对象
 *
 * @author ：yzhang
 * @since ：2022/1/28 21:56
 *  1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    /**
     * 成功调用的返回的默认信息
     */
    private static final String SUCCESS_MESSAGE = "操作成功";

    /**
     * 调用失败返回的错误信息
     */
    private static final String ERROR_MESSAGE = "操作失败";


    private String requestTime;

    /**
     * 状态代码
     */
    private String code;

    /**
     * 状态信息
     */
    private String message;

    /**
     * 业务结果对象,可以是业务成功对象，也可以是业务错误对象
     */
    private T data;

    /**
     * 构建成功的业务返回结果
     *
     * @param data 业务结果数据
     * @return Result 统一结果对象
     */
    public static <T> Result<T> success(T data) {
        return of(HTTP_STATUS_200.getCode(), SUCCESS_MESSAGE, data);
    }

    /**
     * 默认调用成功，且无返回值
     *
     * @return
     */
    public static Result<String> success() {
        return of(HTTP_STATUS_200.getCode(), SUCCESS_MESSAGE, null);
    }

    /**
     * 构建业务调用异常的返回结果
     *
     * @param code    错误状态码
     * @param message 错误信息
     * @param data    错误信息详情
     * @return Result 统一结果对象
     */
    public static <T> Result<T> error(String code, String message, T data) {
        return of(code, StringUtils.isNotBlank(message) ? message : ERROR_MESSAGE, data);
    }


    /**
     * 构建业务调用异常的返回结果
     *
     * @param code    错误状态码
     * @param message 错误信息
     * @return Result 统一结果对象
     */
    public static Result error(String code, String message) {
        return of(code, StringUtils.isNotBlank(message) ? message : ERROR_MESSAGE, null);
    }


    /**
     * 内部私有构建方法
     *
     * @param code    状态码
     * @param message 状态信息
     * @param data    正常业务数据或非正常业务错误信息
     * @return Result 统一结果对象
     */
    public static <T> Result<T> of(String code, String message, T data) {
        Result result = new Result<T>();
        result.setRequestTime(now());
        result.setCode(code);
        result.setData(data);
        result.setMessage(message);
        return result;
    }

    /**
     * 获取当前时间戳
     *
     * @return
     */
    private static String now() {
        return LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli() + "";
    }

    public boolean isSuccess(){
        return HTTP_STATUS_200.getCode().equals(this.getCode());

    }
}

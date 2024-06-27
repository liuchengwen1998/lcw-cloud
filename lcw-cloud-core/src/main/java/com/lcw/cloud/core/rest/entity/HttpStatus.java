package com.lcw.cloud.core.rest.entity;

import com.lcw.cloud.core.exception.BizException;
import com.lcw.cloud.core.exception.SysException;

public enum HttpStatus {
    HTTP_STATUS_400("400", "请求参数存在错误。"),
    HTTP_STATUS_401("401", "请求资源无权限访问。"),
    HTTP_STATUS_403("403", "服务器拒绝执行此次请求。"),
    HTTP_STATUS_406("406", "请求资源不存在。"), // 因为404已经被feign异常处理占用了，所以使用这个异常
    HTTP_STATUS_500("500", "服务器发生了错误，请稍后再试。"),
    HTTP_STATUS_505("505", "业务异常"),
    HTTP_STATUS_200("200", "成功");
    HttpStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 状态码
     */
    private String code;

    /**
     * 提示信息
     */
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 构建Biz异常对象{@link BizException}
     *
     * @return BaseBizException 对象
     */
    public BizException bizException() {
        return new BizException(getCode(), getMessage());
    }

    /**
     * 构建Biz异常对象{@link SysException}
     *
     * @return BaseSysException 对象
     */
    public SysException sysException() {
        return new SysException(getCode(), getMessage());
    }

    /**
     * 构建http返回值对象{@link Result}
     *
     * @return result 对象
     */
    public Result result() {
        return Result.error(getCode(), getMessage());
    }

    /**
     * 构建http返回值对象{@link Result}
     *
     * @param message 外部传入的提示信息
     * @return result 对象
     */
    public Result result(String message) {
        return Result.error(getCode(), message);
    }
}

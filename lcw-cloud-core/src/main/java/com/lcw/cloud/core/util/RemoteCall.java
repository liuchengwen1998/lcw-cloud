package com.lcw.cloud.core.util;

import com.lcw.cloud.core.exception.BizException;
import com.lcw.cloud.core.exception.FeignBadRequestException;
import com.lcw.cloud.core.logger.LogFactory;
import com.lcw.cloud.core.rest.entity.HttpStatus;
import com.lcw.cloud.core.rest.entity.Result;

import java.util.function.Supplier;

/**
 * @author yzhang
 * @since 2022/8/21
 * 远程调用工具
 */
public class RemoteCall {


    /**
     * 微服务接口调用并捕获异常
     *
     * @param supplier
     * @param <T>
     * @return
     */
    public static <T> T call(Supplier<T> supplier) {
        try {
            T result = supplier.get();
            // 捕获恒致这边微服务的接口
            if (result instanceof Result) {
                Result res = (Result) result;
                if (!res.getCode().equals(HttpStatus.HTTP_STATUS_200.getCode())) {
                    throw new FeignBadRequestException(res.getCode(), res.getMessage());
                }
                return result;
            }
            // 老的ResultWrapper微服务是直接抛出异常的，下面的Exception可以直接捕获
            return result;
        } catch (Exception ex) {
            // 自己人的异常直接继续抛出
            if (ex instanceof FeignBadRequestException || ex instanceof BizException) {
                throw ex;
            }
            LogFactory.bizErr("调用接口请求失败，失败的原因为" + ex.getMessage(), ex);
            throw new FeignBadRequestException(HttpStatus.HTTP_STATUS_505.getCode(), HttpStatus.HTTP_STATUS_505.getMessage());
        }
    }
}

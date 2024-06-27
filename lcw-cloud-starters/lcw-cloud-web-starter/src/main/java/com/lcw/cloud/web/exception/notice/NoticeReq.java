package com.lcw.cloud.web.exception.notice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yzhang
 * @since 2023/1/11
 * 通知请求信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeReq {

    /**
     * 状态代码
     */
    private String code;

    /**
     * 状态信息
     */
    private String message;


}

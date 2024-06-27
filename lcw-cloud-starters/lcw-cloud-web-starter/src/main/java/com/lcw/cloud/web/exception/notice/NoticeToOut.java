package com.lcw.cloud.web.exception.notice;

/**
 * @author yzhang
 * @since 2023/1/11
 * 外部接口通知接口
 */
public interface NoticeToOut {


    /**
     * 异常信息外部通知
     *
     * @param req
     */
    void notice(NoticeReq req);


    /**
     * 通知系统
     *
     * @return 返回当前异常的系统
     */
    String noticeSystem();

    /**
     * 组装通知的消息
     * @param req
     * @return
     */
    default String assemblyNoticeMsg(NoticeReq req) {
        return "异常系统为：" + noticeSystem()
                + ",异常错误码为：" + req.getCode()
                + ",异常消息为：" + req.getMessage();
    }

}

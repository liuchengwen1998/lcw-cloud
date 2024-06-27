package com.lcw.cloud.web.exception;

import com.lcw.cloud.web.exception.notice.NoticeToOutContext;

/**
 * @author yzhang
 * @since 2023/1/11
 */
public abstract class AbstractExceptionMapper {

    /**
     * 引入异常模块通知接口
     */
    protected NoticeToOutContext noticeToOutContext;

    public NoticeToOutContext getNoticeToOutContext() {
        return noticeToOutContext;
    }

    public void setNoticeToOutContext(NoticeToOutContext noticeToOutContext) {
        this.noticeToOutContext = noticeToOutContext;
    }
}

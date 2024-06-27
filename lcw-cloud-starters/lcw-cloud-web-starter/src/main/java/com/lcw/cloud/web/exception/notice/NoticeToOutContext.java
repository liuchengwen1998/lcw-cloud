package com.lcw.cloud.web.exception.notice;

import com.lcw.cloud.core.logger.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author yzhang
 * @since 2023/1/11
 * 外部通知上下文
 */
public class NoticeToOutContext {


    private List<NoticeToOut> noticeList = new ArrayList<>();

    /**
     * 自定义线程池
     */
    private static final ExecutorService executorService = new ThreadPoolExecutor(Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors() * 2,
            30, TimeUnit.MINUTES,
            new ArrayBlockingQueue(1000),
            Executors.defaultThreadFactory(),
            new ThreadPoolExecutor.DiscardOldestPolicy());


    /**
     * 异常信息外部通知
     *
     * @param code 错误码
     * @param msg  错误消息
     */
    public void notice(String code, String msg) {
        try {
            NoticeReq req = new NoticeReq(code, msg);
            noticeList.forEach(e -> executorService.submit(() -> e.notice(req)));
        } catch (Exception ex) {
            LogFactory.bizErr("发送异常通知失败....失败原因为" + ex.getMessage(), ex);
        }
    }

    public List<NoticeToOut> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeToOut> noticeList) {
        this.noticeList = noticeList;
    }
}

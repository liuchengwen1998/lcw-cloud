package com.lcw.cloud.web.security.session;

import com.lcw.cloud.redis.util.RedisUtils;

import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yzhang
 * @since 2022/8/9
 * redis存储用户
 */
public class RedisUserSessionStore implements ISessionStore {


    /**
     * 存储会话的key
     */
    private static final String OMC_SESSION_FOLDER = "omc:user:%s:session%s";

    /**
     * 会话默认存储过期时间 6个小时
     */
    private static final Integer SESSION_DEFAULT_TIMEOUT = 60 * 6;


    private String getKey(Long sessionId, String subSystemCode) {
        return String.format(OMC_SESSION_FOLDER, subSystemCode, sessionId);
    }

    /**
     * 自定义线程池，核心线程数为2，最大为4，不宜设置过大，防止占用过多的线程
     * 使用1000长度的队列，策略是超过就直接丢弃
     */
    ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 4, 10,
            TimeUnit.SECONDS, new ArrayBlockingQueue(1000),
            r -> new Thread(r, "刷新用户redis的过期时间"), new ThreadPoolExecutor.DiscardOldestPolicy());


    @Override
    public Boolean save(Authentication authentication) {
        RedisUtils.set(getKey(authentication.getUserId(), authentication.getSubSystemCode()), authentication, SESSION_DEFAULT_TIMEOUT * 60);
        return true;
    }

    @Override
    public void refresh(Long sessionId, String subSystemCode) {
        executor.execute(() -> {
            RedisUtils.expire(getKey(sessionId, subSystemCode), SESSION_DEFAULT_TIMEOUT * 60);
        });
    }

    @Override
    public Authentication get(Long sessionId, String subSystemCode) {
        Object obj = RedisUtils.get(getKey(sessionId, subSystemCode));
        if (Objects.nonNull(obj)) {
            return (Authentication) obj;
        }
        return null;
    }

    @Override
    public void remove(Long sessionId, String subSystemCode) {
        RedisUtils.del(getKey(sessionId, subSystemCode));
    }
}

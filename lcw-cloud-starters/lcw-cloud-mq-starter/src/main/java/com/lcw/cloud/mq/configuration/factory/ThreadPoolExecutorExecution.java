package com.lcw.cloud.mq.configuration.factory;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * ClassName: ThreadPoolExecutorExecution
 * Description:
 * date: 2019/4/27 19:55
 *
 * @author ThierrySquirrel
 * @since JDK 1.8
 */
public class ThreadPoolExecutorExecution {
	private ThreadPoolExecutorExecution() {
	}

	public static void statsThread(ThreadPoolExecutor threadPoolExecutor, Runnable runnable) {
		threadPoolExecutor.execute(runnable);
	}
}

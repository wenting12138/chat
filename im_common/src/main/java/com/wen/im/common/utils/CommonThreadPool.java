package com.wen.im.common.utils;


import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class CommonThreadPool {

    private final static Logger log = LoggerFactory.getLogger(CommonThreadPool.class);

    private ThreadPoolExecutor workerExecutor;

    public CommonThreadPool() {
        initWorkExecutor();
    }

    private void initWorkExecutor() {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setUncaughtExceptionHandler((thread, throwable) -> {
                    log.error("common executor has uncaughtException.");
                    log.error(throwable.getMessage(), throwable);
                })
                .setDaemon(true)
                .setNameFormat("common-worker-%d")
                .build();
        workerExecutor = new ThreadPoolExecutor(2,
                Integer.MAX_VALUE,
                10,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * Run the task thread
     * @param runnable Task    
     * @throws RejectedExecutionException when thread pool full    
     */
    public void execute(Runnable runnable) throws RejectedExecutionException {
        workerExecutor.execute(runnable);
    }

    public ExecutorService getPool(){
        return workerExecutor;
    }

    public void destroy() throws Exception {
        if (workerExecutor != null) {
            workerExecutor.shutdownNow();
        }
    }
}
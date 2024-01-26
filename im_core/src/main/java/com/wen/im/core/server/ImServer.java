package com.wen.im.core.server;

import com.wen.im.common.config.ImServerConfig;
import com.wen.im.common.utils.RequestCode;
import org.apache.rocketmq.client.exception.MQClientException;

import java.util.concurrent.ExecutorService;

/**
 * @author wenting
 */
public interface ImServer {

    /**
     * register remoting processor
     * 根据不同的type注册不同的processor
     * @param requestCode type
     * @param processor remoting processor
     */
    void registerProcessor(final int requestCode, final NettyWebSocketRemotingProcessor processor,
                           final ExecutorService executor);
    void registerProcessor(final RequestCode requestCode, final NettyWebSocketRemotingProcessor processor,
                           final ExecutorService executor);
    public void registerDefaultProcessor(NettyWebSocketRemotingProcessor processor, ExecutorService executor);


    /**
     *  im服务器启动
     * @return
     */
    public boolean start() throws MQClientException;

    /**
     *  im服务器 停止
     */
    public void shutdown();

    /**
     *  服务停止的钩子方法
     */
    public void registerShutdownHook(Runnable task);
}

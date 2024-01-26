package com.wen.im.core.server;

import io.netty.channel.Channel;

/**
 *  interface some method from ChannelInboundHandler
 */
public interface NettyEventListener {

    /**
     *  通道连接事件
     * @param channel
     * @throws Exception
     */
    default void onChannelConnection(final Channel channel) throws Exception {
    }

    /**
     *  通道关闭事件
     * @param channel
     * @throws Exception
     */
    default void onChannelClose(final Channel channel) throws Exception {
    }

    /**
     *  通道异常事件
     * @param channel
     * @param cause
     * @throws Exception
     */
    default void onChannelException(Channel channel, Throwable cause) throws Exception {

    }
}
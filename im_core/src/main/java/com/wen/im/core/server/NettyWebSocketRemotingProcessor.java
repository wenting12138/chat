package com.wen.im.core.server;

import com.wen.im.common.utils.RequestCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * netty remoting processor
 * @author wenting
 */
public interface NettyWebSocketRemotingProcessor {
    /**
     *  前端请求调用
     * @param ctx
     * @param request
     * @return
     */
    default ImResponse handleFrontend(ChannelHandlerContext ctx, ImRequest request) throws Exception{
        return ImResponse.result(null, null);
    }

    /**
     *  后端请求调用
     * @param request
     */
    default void handleBackendRequest(ImRequest request) throws Exception{

    }

}
package com.wen.im.core.processors;

import com.wen.im.common.utils.ResponseCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.server.NettyImServer;
import com.wen.im.core.server.NettyWebSocketRemotingProcessor;
import io.netty.channel.ChannelHandlerContext;

/**
 * @author wenting
 */
public class PingProcessor implements NettyWebSocketRemotingProcessor {
    private final NettyImServer server;
    public PingProcessor(NettyImServer server){
        this.server = server;
    }
    @Override
    public ImResponse handleFrontend(ChannelHandlerContext ctx, ImRequest request) {
        return ImResponse.result(ResponseCode.PONG, "");
    }
}

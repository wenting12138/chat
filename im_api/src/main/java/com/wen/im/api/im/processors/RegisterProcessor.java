package com.wen.im.api.im.processors;

import com.wen.im.common.event.Event;
import com.wen.im.common.event.EventType;
import com.wen.im.common.event.IMEvent;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.common.utils.ResponseCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.server.NettyImServer;
import com.wen.im.core.server.NettySpringWebsocketRemotingProcessor;
import com.wen.im.core.server.NettyWebSocketRemotingProcessor;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @author wenting
 */
@Component
public class RegisterProcessor implements NettySpringWebsocketRemotingProcessor {
    private  NettyImServer server;

    @Override
    public void setServer(NettyImServer server) {
        this.server = server;
    }

    @Override
    public ImResponse handleFrontend(ChannelHandlerContext ctx, ImRequest request) {
        boolean b = this.server.getClientService().registerClient(ctx.channel(), request);
        return ImResponse.result(b ? ResponseCode.REGISTER_OK : ResponseCode.REGISTER_ERROR, "");
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.REGISTER;
    }

}

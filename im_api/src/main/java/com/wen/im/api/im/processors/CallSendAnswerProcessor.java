package com.wen.im.api.im.processors;

import com.wen.im.common.utils.RequestCode;
import com.wen.im.common.utils.ResponseCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.server.NettyImServer;
import com.wen.im.core.server.NettySpringWebsocketRemotingProcessor;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

/**
 * @author wenting
 */
@Component
public class CallSendAnswerProcessor implements NettySpringWebsocketRemotingProcessor {

    private  NettyImServer server;

    @Override
    public void setServer(NettyImServer server) {
        this.server = server;
    }

    @Override
    public ImResponse handleFrontend(ChannelHandlerContext ctx, ImRequest request) throws Exception{
        server.sendMqMsg(request);
        return null;
    }

    @Override
    public void handleBackendRequest(ImRequest request) {
        com.alibaba.fastjson2.JSONObject body = com.alibaba.fastjson2.JSONObject.from(request.getBody());
        String calledUid = body.getString("calledUid");
        String callerUid = body.getString("callerUid");
        ImResponse response = ImResponse.result(ResponseCode.CALL_SEND_ANSWER, request.getBody(), "");
        server.getClientService().sendMsg(callerUid, response);
    }


    @Override
    public RequestCode getRequestCode() {
        return RequestCode.SEND_ANSWER_REQ;
    }

}

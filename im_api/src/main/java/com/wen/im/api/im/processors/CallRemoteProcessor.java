package com.wen.im.api.im.processors;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wen.im.common.model.dto.chat.body.RoomChatBody;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.common.utils.ResponseCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.server.NettyImServer;
import com.wen.im.core.server.NettySpringWebsocketRemotingProcessor;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenting
 */
@Component
public class CallRemoteProcessor implements NettySpringWebsocketRemotingProcessor {

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
        String calledUid = body.getString("calledUid"); // 接收者
        String callerUid = body.getString("callerUid");
        ImResponse response = ImResponse.result(ResponseCode.CALL_REMOTE_RES, request.getBody(), "");
        server.getClientService().sendMsg(calledUid, response);
    }


    @Override
    public RequestCode getRequestCode() {
        return RequestCode.CALL_REMOTE_REQ;
    }

}

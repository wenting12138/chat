package com.wen.im.api.im.processors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wen.im.common.model.dto.chat.body.SingleChatBody;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.server.NettyImServer;
import com.wen.im.core.server.NettySpringWebsocketRemotingProcessor;
import com.wen.im.core.server.NettyWebSocketRemotingProcessor;
import org.springframework.stereotype.Component;

/**
 * @author wenting
 */
@Component
public class NewFriendProcessor implements NettySpringWebsocketRemotingProcessor {
    private  NettyImServer server;

    @Override
    public void setServer(NettyImServer server) {
        this.server = server;
    }

    @Override
    public void handleBackendRequest(ImRequest request) {
        SingleChatBody body = JSONObject.parseObject(JSON.toJSONString(request.getBody()), SingleChatBody.class);
        ImResponse response = ImResponse.result(request.getCode(), body.getBody(), "新好友申请");
        this.server.getClientService().sendMsg(String.valueOf(body.getUid()), response);
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.REQUEST_NEW_FRIEND;
    }
}

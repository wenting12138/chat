package com.wen.im.api.im.processors;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wen.im.api.subsrcibes.springEvent.event.NewFriendSessionEvent;
import com.wen.im.common.model.dto.chat.body.RoomChatBody;
import com.wen.im.common.model.dto.event.NewFriendSessionDTO;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.common.utils.ResponseCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.server.NettyImServer;
import com.wen.im.core.server.NettySpringWebsocketRemotingProcessor;
import com.wen.im.core.server.NettyWebSocketRemotingProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenting
 */
@Component
public class NewFriendSessionProcessor implements NettySpringWebsocketRemotingProcessor {
    private  NettyImServer server;

    @Override
    public void setServer(NettyImServer server) {
        this.server = server;
    }

    @Override
    public void handleBackendRequest(ImRequest request) {
        RoomChatBody body = JSON.parseObject(JSON.toJSONString(request.getBody()), RoomChatBody.class);
        ImResponse response = ImResponse.result(ResponseCode.NEW_FRIEND_SESSION, body.getBody(), "新好友会话");
        List<String> allUid = body.getUidList().stream().map(String::valueOf).collect(Collectors.toList());
        this.server.getClientService().sendMsg(allUid, response);
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.NEW_FRIEND_SESSION;
    }
}

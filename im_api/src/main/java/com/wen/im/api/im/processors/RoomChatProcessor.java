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
import com.wen.im.core.server.NettyWebSocketRemotingProcessor;
import io.netty.channel.ChannelHandlerContext;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wenting
 */
@Component
public class RoomChatProcessor implements NettySpringWebsocketRemotingProcessor {

    private  NettyImServer server;

    @Override
    public void setServer(NettyImServer server) {
        this.server = server;
    }

    @Override
    public void handleBackendRequest(ImRequest request) {
        RoomChatBody chatBody = JSON.parseObject(JSON.toJSONString(request.getBody()), RoomChatBody.class);
        List<String> uidList = chatBody.getUidList().stream().map(String::valueOf).collect(Collectors.toList());
        ImResponse response = ImResponse.result(ResponseCode.SINGLE_CHAT_MSG, chatBody.getBody(), "发送成功");
        server.getClientService().sendMsg(uidList, response);
    }

    @Override
    public RequestCode getRequestCode() {
        return RequestCode.ROOM_CHAT;
    }
}

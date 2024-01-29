package com.wen.im.api.subsrcibes.springEvent.listener;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.subsrcibes.springEvent.event.ApplyFriendEvent;
import com.wen.im.api.subsrcibes.springEvent.event.SendMsgEvent;
import com.wen.im.common.model.dto.chat.body.RoomChatBody;
import com.wen.im.common.model.entity.Room;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.server.NettyImServer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * @author wenting
 */
@Component
public class SendMsgListener implements ApplicationListener<SendMsgEvent> {
    @Autowired
    private NettyImServer nettyImServer;
    @Override
    public void onApplicationEvent(SendMsgEvent event) {
        try {
            Room room = event.getRoom();
            // 发送消息
            ImRequest imRequest = ImRequest.createRequest(
                    RequestCode.SINGLE_CHAT.getCode(),
                    event.getUid()
            );
            imRequest.setBody(new RoomChatBody(event.getBody(), event.getUidListAll()));
            nettyImServer.sendMqMsg(imRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

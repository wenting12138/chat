package com.wen.im.api.subsrcibes.springEvent.listener;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.subsrcibes.springEvent.event.ApplyFriendEvent;
import com.wen.im.common.model.dto.chat.body.SingleChatBody;
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
public class ApplyFriendListener implements ApplicationListener<ApplyFriendEvent> {
    @Autowired
    private NettyImServer nettyImServer;
    @Override
    public void onApplicationEvent(ApplyFriendEvent event) {
        JSONObject body = new JSONObject();
        body.put("uid", event.getUserApply().getTargetId());
        body.put("unreadCount", event.getUnreadCount());
        ImRequest request = ImRequest.createRequest(RequestCode.REQUEST_NEW_FRIEND,
                event.getUserApply().getUid());
        request.setBody(new SingleChatBody(body, event.getUserApply().getTargetId()));
        try {
            nettyImServer.sendMqMsg(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

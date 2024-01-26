package com.wen.im.api.subsrcibes.springEvent.listener;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.subsrcibes.springEvent.event.ApplyFriendEvent;
import com.wen.im.api.subsrcibes.springEvent.event.NewFriendSessionEvent;
import com.wen.im.common.model.dto.chat.body.RoomChatBody;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.server.NettyImServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author wenting
 */
@Component
public class NewFriendSessionListener implements ApplicationListener<NewFriendSessionEvent> {
    @Autowired
    private NettyImServer nettyImServer;
    @Override
    public void onApplicationEvent(NewFriendSessionEvent event) {
        ImRequest request = ImRequest.createRequest(RequestCode.NEW_FRIEND_SESSION,
                event.getEvent().getUid());
        request.setBody(new RoomChatBody(event.getEvent(), event.getGroupAllUidList()));
        try {
            nettyImServer.sendMqMsg(request);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

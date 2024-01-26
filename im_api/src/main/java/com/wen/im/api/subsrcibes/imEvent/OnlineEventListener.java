package com.wen.im.api.subsrcibes.imEvent;

import com.wen.im.api.mapper.UserMapper;
import com.wen.im.api.service.UserService;
import com.wen.im.common.event.Event;
import com.wen.im.common.event.EventType;
import com.wen.im.common.event.IMEvent;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wenting
 */
@Component
public class OnlineEventListener implements IMEvent {

    @Autowired
    private UserMapper userMapper;
    @Override
    public void process(Event event) {
        String uid = event.getUid();
        if (StringUtils.isNotEmpty(uid)) {
            userMapper.online(Long.parseLong(uid));
        }
    }

    @Override
    public int getEventType() {
        return EventType.ON_LINE;
    }
}

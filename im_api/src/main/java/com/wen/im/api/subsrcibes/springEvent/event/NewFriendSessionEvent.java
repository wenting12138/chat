package com.wen.im.api.subsrcibes.springEvent.event;

import com.wen.im.common.model.dto.event.NewFriendSessionDTO;
import com.wen.im.common.model.entity.UserApply;
import org.springframework.context.ApplicationEvent;

import java.io.Serializable;
import java.util.List;

/**
 * @author wenting
 */
public class NewFriendSessionEvent extends ApplicationEvent implements Serializable {
    private NewFriendSessionDTO event;
    private List<Long> groupAllUidList;
    public NewFriendSessionEvent(List<Long> groupAllUidList, NewFriendSessionDTO event) {
        super(event);
        this.event = event;
        this.groupAllUidList = groupAllUidList;
    }

    public List<Long> getGroupAllUidList() {
        return groupAllUidList;
    }

    public void setGroupAllUidList(List<Long> groupAllUidList) {
        this.groupAllUidList = groupAllUidList;
    }

    public NewFriendSessionDTO getEvent() {
        return event;
    }
    public void setEvent(NewFriendSessionDTO event) {
        this.event = event;
    }
}

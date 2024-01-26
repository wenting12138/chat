package com.wen.im.api.subsrcibes.springEvent.event;

import com.wen.im.common.model.entity.UserApply;
import org.springframework.context.ApplicationEvent;

/**
 * @author wenting
 */
public class ApplyFriendEvent extends ApplicationEvent {

    private UserApply userApply;
    private Integer unreadCount;
    public ApplyFriendEvent(UserApply userApply, Integer unreadCount) {
        super(userApply);
        this.userApply = userApply;
        this.unreadCount = unreadCount;
    }

    public Integer getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(Integer unreadCount) {
        this.unreadCount = unreadCount;
    }

    public UserApply getUserApply() {
        return userApply;
    }

    public void setUserApply(UserApply userApply) {
        this.userApply = userApply;
    }
}

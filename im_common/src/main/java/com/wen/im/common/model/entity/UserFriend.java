package com.wen.im.common.model.entity;

/**
 * @author wenting
 */
public class UserFriend {
    private Long id;
    private Long uid;
    private Long friendUid;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getFriendUid() {
        return friendUid;
    }

    public void setFriendUid(Long friendUid) {
        this.friendUid = friendUid;
    }
}

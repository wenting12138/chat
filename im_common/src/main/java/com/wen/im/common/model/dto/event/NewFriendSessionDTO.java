package com.wen.im.common.model.dto.event;

/**
 * @author wenting
 */
public class NewFriendSessionDTO {
    private Long roomId;
    private Long uid;
    /**
     *  1 加入群组，2： 移除群组
     */
    private Integer changeType;
    private Integer activeStatus;
    private Long lastOptTime;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Integer getChangeType() {
        return changeType;
    }

    public void setChangeType(Integer changeType) {
        this.changeType = changeType;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Long getLastOptTime() {
        return lastOptTime;
    }

    public void setLastOptTime(Long lastOptTime) {
        this.lastOptTime = lastOptTime;
    }
}

package com.wen.im.common.model.dto.chat;

import java.io.Serializable;

/**
 * @author wenting
 */
public class NewSessionRequest implements Serializable {
    private Long roomId;
    private Long uid;
    private Integer changeType;
    private Integer activeStatus;
    private Long lastOptTime;

    public NewSessionRequest() {
    }

    public NewSessionRequest(Long roomId, Long uid, Integer changeType, Integer activeStatus, Long lastOptTime) {
        this.roomId = roomId;
        this.uid = uid;
        this.changeType = changeType;
        this.activeStatus = activeStatus;
        this.lastOptTime = lastOptTime;
    }

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

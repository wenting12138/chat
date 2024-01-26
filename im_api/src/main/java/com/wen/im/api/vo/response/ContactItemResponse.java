package com.wen.im.api.vo.response;

import java.util.Date;

/**
 * @author wenting
 */
public class ContactItemResponse {
    private Long uid;
    /** 最后一次上下线时间 */
    private Long lastOptTime;
    /** 在线状态 1在线 2离线 */
    private Integer activeStatus;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getLastOptTime() {
        return lastOptTime;
    }

    public void setLastOptTime(Long lastOptTime) {
        this.lastOptTime = lastOptTime;
    }

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }
}

package com.wen.im.common.model.dto.msg;

import java.io.Serializable;
import java.util.Date;

public class MsgRecall implements Serializable {
    private static final long serialVersionUID = 1L;
    //撤回消息的uid
    private Long recallUid;
    //撤回的时间点
    private Date recallTime;

    public MsgRecall() {
    }

    public MsgRecall(Long recallUid, Date recallTime) {
        this.recallUid = recallUid;
        this.recallTime = recallTime;
    }

    public Long getRecallUid() {
        return recallUid;
    }

    public void setRecallUid(Long recallUid) {
        this.recallUid = recallUid;
    }

    public Date getRecallTime() {
        return recallTime;
    }

    public void setRecallTime(Date recallTime) {
        this.recallTime = recallTime;
    }
}
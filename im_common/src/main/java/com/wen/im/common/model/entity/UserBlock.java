package com.wen.im.common.model.entity;

import java.util.Date;

/**
 * @author wenting
 */
public class UserBlock {
    private Long id;
    private Long uid;
    private Long blockUid;
    private Date createTime;

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


    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

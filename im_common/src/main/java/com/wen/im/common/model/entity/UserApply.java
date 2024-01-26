package com.wen.im.common.model.entity;

import java.util.Date;

/**
 * @author wenting
 */
public class UserApply {

    private Long id;
    /**
     * 申请人uid
     */
    private Long uid;

    /**
     * 申请类型 1加好友, 2 邀请进群
     */
    private Integer type;

    /**
     * 接收人uid
     */
    private Long targetId;

    /**
     * 申请信息
     */
    private String msg;

    /**
     * 申请状态 1待审批 2同意
     */
    private Integer status;

    /**
     * 阅读状态 1未读 2已读
     */
    private Integer readStatus;

    /**
     * 额外信息
     */
    private String extra;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 修改时间
     */
    private Date updateTime;

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(Integer readStatus) {
        this.readStatus = readStatus;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}

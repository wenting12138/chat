package com.wen.im.common.model.dto.event;

public class UserItem {
    /** 在线状态 */
    private Integer activeStatus;
    /** 头像 */
    private String avatar;
    /** 最后一次上下线时间 */
    private Long lastOptTime;
    /** 用户名称 */
    private String name;
    /** 角色ID */
    private Integer roleId;
    /** uid */
    private Long uid;

    public Integer getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(Integer activeStatus) {
        this.activeStatus = activeStatus;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getLastOptTime() {
        return lastOptTime;
    }

    public void setLastOptTime(Long lastOptTime) {
        this.lastOptTime = lastOptTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}

package com.wen.im.api.vo.response;

import java.io.Serializable;

/**
 * @author wenting
 */
public class GroupDetailsResponse implements Serializable {
    /** 群头像 */
    private String avatar;
    /** 群名称 */
    private String groupName;
    /** 在线人数 */
    private Integer onlineNum;
    /** 成员角色 1群主 2管理员 3普通成员 4踢出群聊 */
    private Integer role;
    /** 房间id */
    private Long roomId;

    public GroupDetailsResponse() {
    }


    public GroupDetailsResponse(String avatar, String groupName, Integer onlineNum, Integer role, Long roomId) {
        this.avatar = avatar;
        this.groupName = groupName;
        this.onlineNum = onlineNum;
        this.role = role;
        this.roomId = roomId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }
}

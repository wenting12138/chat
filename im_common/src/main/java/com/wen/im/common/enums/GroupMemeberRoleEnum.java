package com.wen.im.common.enums;

/**
 * @author wenting
 */

public enum GroupMemeberRoleEnum {
    /**
     *  群主
     */
    GROUP_LEADER(1),
    /**
     *  管理员
     */
    GROUP_MANAGER(2),
    /**
     * 普通群员
     */
    GROUP_MEMBER(3),
    /**
     * 踢出群聊
     */
    GROUP_EXIT(4),

    ;
    int role;

    GroupMemeberRoleEnum(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }
}

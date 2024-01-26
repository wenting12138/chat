package com.wen.im.common.enums;

/**
 * @author wenting
 */

public enum UserPowerEnum {
    /**
     *  用户
     */
    USER(0),
    /**
     *  管理员
     */
    ADMIN(1),
    ;
    int type;

    UserPowerEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

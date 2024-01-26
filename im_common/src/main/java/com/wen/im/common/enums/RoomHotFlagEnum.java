package com.wen.im.common.enums;

/**
 * @author wenting
 */

public enum RoomHotFlagEnum {
    /**
     *  聊天群所有人不可见
     */
    NONE(0),
    /**
     *  聊天群所有人见
     */
    ALL(1),
    ;
    int type;

    RoomHotFlagEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

package com.wen.im.common.enums;

/**
 * @author wenting
 */

public enum RoomTypeEnum {
    /**
     *  群聊
     */
    ROOM_CHAT(1),
    /**
     *  单聊
     */
    SINGLE_CHAT(2),
    /**
     *  会议
     */
    MEET(3),
    ;
    int type;

    RoomTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

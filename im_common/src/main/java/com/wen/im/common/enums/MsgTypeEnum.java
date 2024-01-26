package com.wen.im.common.enums;

/**
 * @author wenting
 */

public enum MsgTypeEnum {
    /** 未知 */
    UNKNOWN(0),
    /** 文本 */
    TEXT(1),
    /** 撤回 */
    RECALL(2),
    /** 图片 */
    IMAGE(3),
    /** 文件 */
    FILE(4),
    /** 语音 */
    VOICE(5),
    /** 视频 */
    VIDEO(6),
    /** 表情包 */
    EMOJI(7),
    /** 系统消息 */
    SYSTEM(8),
    ;
    int type;

    MsgTypeEnum(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }
}

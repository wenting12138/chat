package com.wen.im.common.utils;

/**
 * @author wenting
 */
public enum ResponseCode {
    /**
     * pong
     */
    PONG(39999),
    /**
     * 客户端注册成功 code
     */
    REGISTER_OK(40000),
    /**
     * 客户端注册失败 code
     */
    REGISTER_ERROR(40001),

    /**
     *  接收请求
     */
    CALL_REMOTE_RES(40002),
    /**
     *  同意
     */
    CALL_ACCEPT_RES(40003),
    /**
     *  发送offer
     */
    CALL_SEND_OFFER(40004),
    /**
     *  发送 answer
     */
    CALL_SEND_ANSWER(40005),
    /**
     *  发送 candidate: A -> B
     */
    CALL_SEND_CANDIDATE(40006),
    // 发送 candidate  B - >
    CALL_SEND_CANDIDATE_2(40007),
    // 挂断
    CALL_SEND_HANGUP(40008),
    CALL_SEND_REJECT(40009),
    CALL_SEND_CANCEL(40010),
    CALL_SEND_BUSY(40011),
    /**
     * 单对单消息
     */
    SINGLE_CHAT_MSG(40012),
    /**
     *  新好友申请
     */
    REQUEST_NEW_FRIEND(40013),
    /**
     *  新好友申请
     */
    NEW_FRIEND_SESSION(40014),
    ;
    int code;

    ResponseCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

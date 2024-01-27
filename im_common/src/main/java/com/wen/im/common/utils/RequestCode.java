package com.wen.im.common.utils;

/**
 * @author wenting
 */
public enum RequestCode {
    /**
     *  用户上下线事件
     */
    ON_OFF_LINE(5),
    /**
     *  新好友申请
     */
    REQUEST_NEW_FRIEND(10),
    /**
     *  新好友会话
     */
    NEW_FRIEND_SESSION(11),
    /**
     *  客户端注册 ping
     */
    PING(10000),
    /**
     *  客户端注册 code
     */
    REGISTER(10001),
    /**
     *  单对单聊天
     */
    SINGLE_CHAT(10002),
    /**
     *  登录成功
     */
    LOGIN_SUCCESS(10003),
    /**
     *  群聊聊天
     */
    ROOM_CHAT(10004),

    /**
     *  向信令服务器发送发起请求的事件
     */
    CALL_REMOTE_REQ(50000),
    /** 6.向信令服务器发送发起同意的事件 */
    ACCEPT_CALL_REQ(50001),
    /** 6.发送offer */
    SEND_OFFER_REQ(50002),
    /** 发送answer信令 */
    SEND_ANSWER_REQ(50003),
    // 发送 candidate  A -> B
    SEND_CANDIDATE_REQ(50004),
    // 发送 candidate B -> A
    SEND_CANDIDATE_2_REQ(50005),
    SEND_HANGUP_REQ(50006),
    SEND_REJECT_REQ(50007),
    SEND_CANCEL_REQ(50008),
    ;
    int code;

    RequestCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}

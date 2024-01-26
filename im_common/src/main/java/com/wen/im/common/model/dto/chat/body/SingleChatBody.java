package com.wen.im.common.model.dto.chat.body;

import java.io.Serializable;
import java.util.List;

/**
 * @author wenting
 */
public class SingleChatBody implements Serializable {
    private Object body;
    private Long uid;

    public SingleChatBody() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public SingleChatBody(Object body, Long uid) {
        this.body = body;
        this.uid = uid;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

}

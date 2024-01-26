package com.wen.im.common.model.dto.chat.body;

import com.wen.im.common.serirals.Serializer;

import java.io.Serializable;
import java.util.List;

/**
 * @author wenting
 */
public class RoomChatBody implements Serializable {
    private Object body;
    private List<Long> uidList;

    public RoomChatBody() {
    }

    public RoomChatBody(Object body, List<Long> uidList) {
        this.body = body;
        this.uidList = uidList;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public List<Long> getUidList() {
        return uidList;
    }

    public void setUidList(List<Long> uidList) {
        this.uidList = uidList;
    }
}

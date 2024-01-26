package com.wen.im.api.subsrcibes.springEvent.event;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.common.model.entity.Room;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * @author wenting
 */
public class SendMsgEvent extends ApplicationEvent {
    private JSONObject body;
    private Room room;
    private Long uid;
    private List<Long> uidListAll;

    public SendMsgEvent(JSONObject body, Room room, List<Long> uidListAll, Long uid) {
        super(body);
        this.body = body;
        this.room = room;
        this.uid = uid;
        this.uidListAll = uidListAll;
    }

    public List<Long> getUidListAll() {
        return uidListAll;
    }

    public void setUidListAll(List<Long> uidListAll) {
        this.uidListAll = uidListAll;
    }

    public JSONObject getBody() {
        return body;
    }

    public void setBody(JSONObject body) {
        this.body = body;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}

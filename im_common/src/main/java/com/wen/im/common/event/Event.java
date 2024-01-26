package com.wen.im.common.event;

public class Event {
    private String uid;
    private String clientVersion;
    private String clientIdentify;

    public Event(String uid, String clientVersion, String clientIdentify) {
        this.uid = uid;
        this.clientVersion = clientVersion;
        this.clientIdentify = clientIdentify;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getClientIdentify() {
        return clientIdentify;
    }

    public void setClientIdentify(String clientIdentify) {
        this.clientIdentify = clientIdentify;
    }
}

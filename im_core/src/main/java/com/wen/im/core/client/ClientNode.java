package com.wen.im.core.client;

import com.alibaba.fastjson2.JSON;
import com.wen.im.core.protocols.ImResponse;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * @author wenting
 */
public class ClientNode implements IClient{
    private Channel ch;
    private String uid;
    private String clientVersion;
    private String clientIdentify;

    public ClientNode(Channel ch, String uid, String clientVersion, String clientIdentify) {
        this.ch = ch;
        this.uid = uid;
        this.clientVersion = clientVersion;
        this.clientIdentify = clientIdentify;
    }

    @Override
    public void writeMessage(ImResponse response) {
        this.ch.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(response)));
    }

    @Override
    public void close() {
        this.ch.close();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Channel getCh() {
        return ch;
    }

    public void setCh(Channel ch) {
        this.ch = ch;
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

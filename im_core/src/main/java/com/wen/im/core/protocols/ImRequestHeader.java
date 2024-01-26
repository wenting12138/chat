package com.wen.im.core.protocols;

import com.alibaba.fastjson2.JSON;
import com.wen.im.common.constants.NettyConstant;

/**
 * @author wenting
 */
public class ImRequestHeader {
    /**
     *  客户端版本
     */
    private String clientVersion;
    /**
     * 客户端标识
     */
    private String clientIdentify;
    /**
     * 用户uid
     */
    private String uid;

    public static ImRequestHeader defaultHeader(Long uid) {
        ImRequestHeader header = new ImRequestHeader();
        header.setUid(String.valueOf(uid));
        header.setClientVersion(NettyConstant.CLIENT_VERSION);
        header.setClientIdentify(NettyConstant.CLIENT_IDENTIFY);
        return header;
    }


    public <T> T encodeRequestHeader(Class<T> clazz){
        return JSON.parseObject(JSON.toJSONString(this), clazz);
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

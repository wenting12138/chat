package com.wen.im.core.service;

import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.server.NettyEventListener;
import io.netty.channel.Channel;

import java.util.List;

/**
 * @author wenting
 */
public interface IClientService extends NettyEventListener {
    /**
     *  移除 client
     * @param ch
     */
    public void removeClient(Channel ch);

    /**
     * 注册 client
     * @param ch
     * @param request
     * @return: 是否注册成功
     */
    public boolean registerClient(Channel ch, ImRequest request);

    /**
     *  向单人发送消息
     * @param uid
     * @param response
     */
    public void sendMsg(String uid, ImResponse response);

    /**
     *  向多人发送消息
     * @param uid
     * @param response
     */
    public void sendMsg(List<String> uid, ImResponse response);
}

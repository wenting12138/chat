package com.wen.im.core.service;

import com.wen.im.common.event.Event;
import com.wen.im.common.event.EventType;
import com.wen.im.common.event.IMEvent;
import com.wen.im.core.client.ClientNode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImRequestHeader;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.server.NettyEventListener;
import com.wen.im.core.server.NettyImServer;
import com.wen.im.core.utils.ChannelAttrKeyUtils;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wenting: 客户端管理服务
 */
public class DefaultClientServiceImpl implements IClientService {
    private static final Logger log = LoggerFactory.getLogger(DefaultClientServiceImpl.class);
    private final Map<String, List<ClientNode>> clientCache;
    private final NettyImServer server;
    public DefaultClientServiceImpl(NettyImServer server){
        this.clientCache = new ConcurrentHashMap<>();
        this.server = server;
    }

    @Override
    public void onChannelClose(Channel channel) {
        this.removeClient(channel);
    }

    @Override
    public void onChannelException(Channel channel, Throwable cause) {
        ImRequestHeader msg = ChannelAttrKeyUtils.parseChannel(channel);
        log.info("连接异常: uid: {}, version: {}, identify: {}",
                msg.getUid(), msg.getClientVersion(), msg.getClientIdentify());
        this.removeClient(channel);
    }

    @Override
    public boolean registerClient(Channel channel, ImRequest request) {
        if (StringUtils.isEmpty(request.getHeader().getUid())) {
            channel.close();
            return false;
        }
        ImRequestHeader header = request.getHeader();
        String uid = header.getUid();
        String version = header.getClientVersion();
        String identify = header.getClientIdentify();
        log.info("上线 uid: {}, version: {}, identify: {}", uid, version, identify);
        channel.attr(ChannelAttrKeyUtils.CLIENT_VERSION_KEY).set(version);
        channel.attr(ChannelAttrKeyUtils.CLIENT_IDENTIFY_KEY).set(identify);
        channel.attr(ChannelAttrKeyUtils.CLIENT_UID_KEY).set(uid);
        ClientNode node = new ClientNode(channel, uid, version, identify);
        registerChannel(node);
        Event event = new Event(uid, version, identify);
        handleOnlineEvent(event);
        return true;
    }

    private void handleOnlineEvent(Event event) {
        IMEvent imEvent = server.getEventList().get(EventType.ON_LINE);
        if (imEvent != null) {
            imEvent.process(event);
        }
    }

    @Override
    public void removeClient(Channel channel){
        String uid = channel.attr(ChannelAttrKeyUtils.CLIENT_UID_KEY).get();
        String version = channel.attr(ChannelAttrKeyUtils.CLIENT_IDENTIFY_KEY).get();
        String identify = channel.attr(ChannelAttrKeyUtils.CLIENT_VERSION_KEY).get();
        channel.close();
        if (StringUtils.isEmpty(uid) || StringUtils.isEmpty(identify)) {
            return;
        }
        log.info("下线 uid: {}, version: {}, identify: {}", uid, version, identify);
        handleOfflineEvent(new Event(uid, version, identify));
        removeEqNode(uid, identify, clientCache.get(uid));
        removeZeroClient();
    }

    private void handleOfflineEvent(Event event) {
        IMEvent imEvent = server.getEventList().get(EventType.OFF_LINE);
        if (imEvent != null) {
            imEvent.process(event);
        }
    }

    private void removeZeroClient() {
        for (String s : clientCache.keySet()) {
            if (clientCache.get(s) != null && clientCache.get(s).size() == 0) {
                clientCache.remove(s);
            }
        }
    }

    private void printOnline(){
        log.info("online num: {}", this.clientCache.size());
    }

    @Override
    public void sendMsg(String toUid, ImResponse response) {
        this.printOnline();
        if (this.clientCache.containsKey(toUid)) {
            List<ClientNode> clientNodeList = this.clientCache.get(toUid);
            log.info("client_no: {}", clientNodeList.size());
            clientNodeList.get(0).writeMessage(response);
        }else {
            log.warn("没有在线: {}", toUid);
        }
    }

    @Override
    public void sendMsg(List<String> toUidList, ImResponse response) {
        for (String toUid : toUidList) {
            sendMsg(toUid, response);
        }
    }


    private void registerChannel(ClientNode node) {
        String uid = node.getUid();
        if (this.clientCache.containsKey(uid)) {
            List<ClientNode> clientNodeList = this.clientCache.get(uid);
            removeEqNode(node, clientNodeList);
            clientNodeList.add(node);
        }else {
            List<ClientNode> clientNodeList = new ArrayList<>();
            clientNodeList.add(node);
            this.clientCache.put(uid, clientNodeList);
        }
    }

    private void removeEqNode(ClientNode node, List<ClientNode> clientNodeList) {
        if (clientNodeList == null || clientNodeList.size() == 0) {
            return;
        }
        List<ClientNode> waitingRemoving = new ArrayList<>();
        for (ClientNode clientNode : clientNodeList) {
            String identify = clientNode.getClientIdentify();
            if (identify.equals(node.getClientIdentify())) {
                waitingRemoving.add(clientNode);
            }
        }
        for (ClientNode clientNode : waitingRemoving) {
            clientNodeList.remove(clientNode);
            clientNode.close();
        }
        waitingRemoving.clear();
    }

    private void removeEqNode(String uid, String identify, List<ClientNode> clientNodeList) {
        if (clientNodeList == null || clientNodeList.size() == 0) {
            return;
        }
        List<ClientNode> waitingRemoving = new ArrayList<>();
        for (ClientNode clientNode : clientNodeList) {
            String identify1 = clientNode.getClientIdentify();
            String uid1 = clientNode.getUid();
            if (uid.equals(uid1) && identify.equals(identify1)) {
                waitingRemoving.add(clientNode);
            }
        }
        for (ClientNode clientNode : waitingRemoving) {
            clientNodeList.remove(clientNode);
            clientNode.close();
        }
        waitingRemoving.clear();
    }


}

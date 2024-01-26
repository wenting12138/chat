package com.wen.im.api.im;

import cn.hutool.extra.spring.SpringUtil;
import com.wen.im.api.im.processors.*;
import com.wen.im.api.subsrcibes.imEvent.OfflineEventListener;
import com.wen.im.api.subsrcibes.imEvent.OnlineEventListener;
import com.wen.im.api.subsrcibes.rocketmq.RedirectMessageSubscribe;
import com.wen.im.common.config.ImServerConfig;
import com.wen.im.common.utils.RequestCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.wen.im.core.server.*;
import org.apache.rocketmq.client.exception.*;
import org.springframework.core.annotation.Order;

import java.util.Map;

/**
 * @author wenting
 */
@Configuration
@ConfigurationProperties(prefix = "ws")
public class ImConfig {
    private int port;
    private String path;
    private String rocketMqNameAddr;
    private String rocketMqSendMessageTopic;
    private String rocketMqSendMessageGroup;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRocketMqNameAddr() {
        return rocketMqNameAddr;
    }

    public void setRocketMqNameAddr(String rocketMqNameAddr) {
        this.rocketMqNameAddr = rocketMqNameAddr;
    }

    public String getRocketMqSendMessageTopic() {
        return rocketMqSendMessageTopic;
    }

    public void setRocketMqSendMessageTopic(String rocketMqSendMessageTopic) {
        this.rocketMqSendMessageTopic = rocketMqSendMessageTopic;
    }

    public String getRocketMqSendMessageGroup() {
        return rocketMqSendMessageGroup;
    }

    public void setRocketMqSendMessageGroup(String rocketMqSendMessageGroup) {
        this.rocketMqSendMessageGroup = rocketMqSendMessageGroup;
    }

//    @Autowired
//    private OfflineEventListener offlineEventListener;
//    @Autowired
//    private OnlineEventListener onlineEventListener;

//    @Bean
//    public NettyImServer nettyImServer() {
//        ImServerConfig serverConfig = new ImServerConfig();
//        serverConfig.setPort(port);
//        serverConfig.setWsPath(path);
//        serverConfig.setRocketMqNameAddr(rocketMqNameAddr);
//        serverConfig.setRocketMqSendMessageTopic(rocketMqSendMessageTopic);
//        serverConfig.setRocketMqSendMessageGroup(rocketMqSendMessageGroup);
//        NettyImServer server = new NettyImServer(serverConfig);
//        server.registerMqListener(new RedirectMessageSubscribe(server));
//        server.registerIMEvent(onlineEventListener);
//        server.registerIMEvent(offlineEventListener);
//        server.registerShutdownHook(server::shutdown);
//        registerProcessors(server);
//
//        start(server);
//        return server;
//    }
//
//    private void registerProcessors(NettyImServer server) {
//        server.registerProcessor(RequestCode.SINGLE_CHAT, new SingleChatProcessor(), null);
//        server.registerProcessor(RequestCode.ROOM_CHAT, new RoomChatProcessor(), null);
//        server.registerProcessor(RequestCode.REGISTER, new RegisterProcessor(), null);
//        server.registerProcessor(RequestCode.REQUEST_NEW_FRIEND, new NewFriendProcessor(), null);
//        server.registerProcessor(RequestCode.NEW_FRIEND_SESSION, new NewFriendSessionProcessor(), null);
//    }

//    private void start(NettyImServer server) {
//        new Thread(){
//            @Override
//            public void run() {
//                try {
//                    server.start();
//                } catch (MQClientException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        }.start();
//    }

}

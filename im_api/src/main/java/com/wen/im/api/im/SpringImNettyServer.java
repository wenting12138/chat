package com.wen.im.api.im;

import cn.hutool.extra.spring.SpringUtil;
import com.wen.im.api.subsrcibes.imEvent.OfflineEventListener;
import com.wen.im.api.subsrcibes.imEvent.OnlineEventListener;
import com.wen.im.api.subsrcibes.rocketmq.RedirectMessageSubscribe;
import com.wen.im.common.config.ImServerConfig;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.core.server.NettyImServer;
import com.wen.im.core.server.NettySpringWebsocketRemotingProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author wenting
 */
@Component
public class SpringImNettyServer extends NettyImServer {
    private final static Logger log = LoggerFactory.getLogger(SpringImNettyServer.class);
    public SpringImNettyServer(ImConfig imConfig) {
        super();
        setConfig(buildServerConfig(imConfig));
        this.initConfigs();
        registerProcessors();
        registerListeners();
        this.registerShutdownHook(this::shutdown);
        start(this);
    }

    private ImServerConfig buildServerConfig(ImConfig imConfig) {
        ImServerConfig serverConfig = new ImServerConfig();
        serverConfig.setPort(imConfig.getPort());
        serverConfig.setWsPath(imConfig.getPath());
        serverConfig.setRocketMqNameAddr(imConfig.getRocketMqNameAddr());
        serverConfig.setRocketMqSendMessageTopic(imConfig.getRocketMqSendMessageTopic());
        serverConfig.setRocketMqSendMessageGroup(imConfig.getRocketMqSendMessageGroup());
        return serverConfig;
    }

    private void start(NettyImServer server) {
        server.getDefaultThreadPool().execute(()-> {
            try {
                server.start();
            } catch (Exception e) {
                log.info("start im server error: {}", e);
            }
        });
    }

    private void registerListeners() {
        this.registerMqListener(new RedirectMessageSubscribe(this));
        this.registerIMEvent(SpringUtil.getBean(OnlineEventListener.class));
        this.registerIMEvent(SpringUtil.getBean(OfflineEventListener.class));
    }

    private void registerProcessors() {
        Map<String, NettySpringWebsocketRemotingProcessor> beans = SpringUtil.getBeansOfType(NettySpringWebsocketRemotingProcessor.class);
        for (NettySpringWebsocketRemotingProcessor processor : beans.values()) {
            RequestCode requestCode = processor.getRequestCode();
            processor.setServer(this);
            log.info("注册ws处理器: {}, processor: {}", requestCode, processor.getClass().getCanonicalName());
            this.registerProcessor(requestCode, processor, processor.getExecutor());
        }
    }
}

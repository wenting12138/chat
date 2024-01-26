package com.wen.im.api.subsrcibes.rocketmq;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.wen.im.api.im.processors.RoomChatProcessor;
import com.wen.im.api.im.processors.SingleChatProcessor;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.common.utils.ResponseCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImResponse;
import com.wen.im.core.server.NettyImServer;
import com.wen.im.core.server.NettyWebSocketRemotingProcessor;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @author wenting
 */
public class RedirectMessageSubscribe implements MessageListenerOrderly {
    private final static Logger log = LoggerFactory.getLogger(RedirectMessageSubscribe.class);
    private final NettyImServer server;

    public RedirectMessageSubscribe(NettyImServer server) {
        this.server = server;
    }

    @Override
    public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
        if (msgs != null && msgs.size() > 0) {
            for (MessageExt msg : msgs) {
                try {
                    String msgBody = new String(msg.getBody(), "utf-8");
                    ImRequest request = JSON.parseObject(msgBody, ImRequest.class);
                    this.handleRequest(request);
                } catch (Exception e) {}
            }
        }
        return ConsumeOrderlyStatus.SUCCESS;
    }

    private void handleRequest(ImRequest request) throws Exception {
        log.info("重定向请求: {}", JSON.toJSONString(request));
        ExecutorService executorService = this.server.getProcessorTable().get(request.getCode()).getObject2();
        NettyWebSocketRemotingProcessor processor = this.server.getProcessorTable().get(request.getCode()).getObject1();
        if (executorService != null) {
            executorService.submit(() -> {
                try {
                    processor.handleBackendRequest(request);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        } else {
            processor.handleBackendRequest(request);
        }
    }
}

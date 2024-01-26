package com.wen.im.common.config;

/**
 * @author wenting
 */
public class ImServerConfig {
    // websocket端口
    private int port = 10036;
    private String wsPath = "/im/chat";
    private String serializeType;

    private String rocketMqSendMessageTopic = "my_im_chat_topic";
    private String rocketMqSendMessageGroup = "my_im_chat";
    private String rocketMqNameAddr = "47.96.80.134:9876";

    public String getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(String serializeType) {
        this.serializeType = serializeType;
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

    public String getRocketMqNameAddr() {
        return rocketMqNameAddr;
    }

    public void setRocketMqNameAddr(String rocketMqNameAddr) {
        this.rocketMqNameAddr = rocketMqNameAddr;
    }

    public String getWsPath() {
        return wsPath;
    }

    public void setWsPath(String wsPath) {
        this.wsPath = wsPath;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}

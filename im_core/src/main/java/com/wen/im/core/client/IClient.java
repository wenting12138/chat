package com.wen.im.core.client;

import com.wen.im.core.protocols.ImResponse;

/**
 * @author wenting
 */
public interface IClient {
    /**
     *  写入信息
     * @param response
     */
    void writeMessage(ImResponse response);

    /**
     *  关闭通道
     */
    void close();
}

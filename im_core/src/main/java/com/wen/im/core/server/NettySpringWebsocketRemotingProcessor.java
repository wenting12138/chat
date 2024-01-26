package com.wen.im.core.server;

import com.wen.im.common.utils.RequestCode;

import java.util.concurrent.ExecutorService;

/**
 * @author wenting
 */
public interface NettySpringWebsocketRemotingProcessor extends NettyWebSocketRemotingProcessor {

    /**
     *  请求标识处理器code
     * @return
     */
    default RequestCode getRequestCode(){
        return null;
    }

    /**
     *  设置server
     * @param server
     */
    default void setServer(NettyImServer server){

    }

    /**
     *  是否使用线程池去处理请求
     * @return
     */
    default ExecutorService getExecutor(){
        return null;
    }

}

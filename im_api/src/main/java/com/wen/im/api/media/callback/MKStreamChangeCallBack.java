package com.wen.im.api.media.callback;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.wen.im.api.media.MediaServerContext;
import com.wen.im.api.media.sdk.callback.IMKStreamChangeCallBack;
import com.wen.im.api.media.sdk.structure.MK_MEDIA_SOURCE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 注册或反注册MediaSource事件广播
 *
 * @author lidaofu
 * @since 2023/11/23
 **/
public class MKStreamChangeCallBack implements IMKStreamChangeCallBack {
    private static final Logger log = LoggerFactory.getLogger(MKStreamChangeCallBack.class);
    public MKStreamChangeCallBack() {
        CallbackThreadInitializer mediaServerLogThread = new CallbackThreadInitializer(true, false, "MediaStreamChangeThread");
        //回调使用同一个线程
        Native.setCallbackThreadInitializer(this, mediaServerLogThread);
    }

    /**
     * 注册或反注册MediaSource事件广播
     * @param regist 注册为1，注销为0
     * @param sender 该MediaSource对象
     */
    public void invoke(int regist, MK_MEDIA_SOURCE sender){
        //这里进行流状态处理
        String stream =     MediaServerContext.ZLM_API.mk_media_source_get_stream(sender);
        String app =  MediaServerContext.ZLM_API.mk_media_source_get_app(sender);
        String schema =   MediaServerContext.ZLM_API.mk_media_source_get_schema(sender);
        log.info("【MediaServer】APP:{} 流:{} 协议：{} {}",app,stream,schema,regist==1?"注册":"注销");
    }
}

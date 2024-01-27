package com.wen.im.api.media.callback;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.wen.im.api.media.MediaServerContext;
import com.wen.im.api.media.sdk.callback.IMKNoReaderCallBack;
import com.wen.im.api.media.sdk.structure.MK_MEDIA_SOURCE;

/**
 * 无人观看回调
 *
 * @author lidaofu
 * @since 2023/11/23
 **/
public class MKNoReaderCallBack implements IMKNoReaderCallBack {
    public MKNoReaderCallBack() {
        //回调使用同一个线程
        Native.setCallbackThreadInitializer(this, new CallbackThreadInitializer(true, false, "MediaNoReaderThread"));
    }

    /**
     * 某个流无人消费时触发，目的为了实现无人观看时主动断开拉流等业务逻辑
     *
     * @param sender 该MediaSource对象
     */
    public void invoke(MK_MEDIA_SOURCE sender) {
        //无人观看时候可以选择关闭这个流资源
        MediaServerContext.ZLM_API.mk_media_source_close(sender,1);
    }
}

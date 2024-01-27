package com.wen.im.api.media.callback;

import com.sun.jna.CallbackThreadInitializer;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.wen.im.api.media.sdk.callback.IMKSourceFindCallBack;
import com.wen.im.api.media.sdk.structure.MK_MEDIA_SOURCE;

/**
 * 寻找流回调
 */
public class MKSourceFindCallBack implements IMKSourceFindCallBack {
    private IMKSourceHandleCallBack imkSourceHandleCallBack;

    public MKSourceFindCallBack(IMKSourceHandleCallBack imkSourceHandleCallBack) {
        this.imkSourceHandleCallBack = imkSourceHandleCallBack;
        //回调使用同一个线程
        Native.setCallbackThreadInitializer(this, new CallbackThreadInitializer(true, false, "MediaSourceFindThread"));
    }

    public void invoke(Pointer user_data, MK_MEDIA_SOURCE ctx) {
        imkSourceHandleCallBack.invoke(ctx);
    }
}
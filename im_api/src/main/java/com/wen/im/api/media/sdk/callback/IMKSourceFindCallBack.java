package com.wen.im.api.media.sdk.callback;

import com.wen.im.api.media.sdk.structure.MK_MEDIA_INFO;
import com.wen.im.api.media.sdk.structure.MK_MEDIA_SOURCE;
import com.wen.im.api.media.sdk.structure.MK_RTSP_AUTH_INVOKER;
import com.wen.im.api.media.sdk.structure.MK_SOCK_INFO;
import com.sun.jna.Callback;
import com.sun.jna.Pointer;

/**
 * 寻找流回调
 */
public interface IMKSourceFindCallBack extends Callback {


    public void invoke(Pointer user_data, MK_MEDIA_SOURCE ctx);
}
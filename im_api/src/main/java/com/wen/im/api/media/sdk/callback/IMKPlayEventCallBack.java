package com.wen.im.api.media.sdk.callback;

import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.wen.im.api.media.sdk.structure.MK_TRACK;

/**
 * 播放结果或播放中断事件的回调
 */
public interface IMKPlayEventCallBack extends Callback {
    /**
     * 播放结果或播放中断事件的回调
     */
    public void invoke(Pointer user_data, int err_code, String err_msg, MK_TRACK tracks[], int track_count);
}
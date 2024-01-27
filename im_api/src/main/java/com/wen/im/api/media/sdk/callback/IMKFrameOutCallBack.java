package com.wen.im.api.media.sdk.callback;


import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.wen.im.api.media.sdk.structure.MK_FRAME;

/**
 * 输出frame回调
 */
public interface IMKFrameOutCallBack extends Callback {
    /**
     * 输出frame回调
     */
    public void invoke(Pointer user_data, MK_FRAME frame);
}
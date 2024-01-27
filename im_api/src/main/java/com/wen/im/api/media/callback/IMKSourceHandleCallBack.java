package com.wen.im.api.media.callback;


import com.wen.im.api.media.sdk.structure.MK_MEDIA_SOURCE;

/**
 * 媒体资源回调接口
 *
 * @author lidaofu
 * @since 2023/11/30
 **/
public interface IMKSourceHandleCallBack {

    void invoke(MK_MEDIA_SOURCE ctx);
}

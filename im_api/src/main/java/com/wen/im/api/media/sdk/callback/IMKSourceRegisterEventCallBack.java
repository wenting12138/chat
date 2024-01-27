package com.wen.im.api.media.sdk.callback;


import com.sun.jna.Callback;
import com.sun.jna.Pointer;
import com.wen.im.api.media.sdk.structure.MK_MEDIA_SOURCE;

/**
 * 生成的MediaSource注册或注销事件
 */
public interface IMKSourceRegisterEventCallBack extends Callback {
    /**
     * 生成的MediaSource注册或注销事件
     *
     * @param user_data 设置回调时的用户数据指针
     * @param sender    生成的MediaSource对象
     * @param regist    1为注册事件，0为注销事件
     */
    public void invoke(Pointer user_data, MK_MEDIA_SOURCE sender, int regist);
}
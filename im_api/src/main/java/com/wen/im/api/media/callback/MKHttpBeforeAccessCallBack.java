
package com.wen.im.api.media.callback;

import com.wen.im.api.media.sdk.callback.IMKHttpBeforeAccessCallBack;
import com.wen.im.api.media.sdk.structure.MK_MEDIA_INFO;
import com.wen.im.api.media.sdk.structure.MK_PARSER;
import com.wen.im.api.media.sdk.structure.MK_RTSP_AUTH_INVOKER;
import com.wen.im.api.media.sdk.structure.MK_SOCK_INFO;

/**
 * 在http文件服务器中,收到http访问文件或目录前的广播,通过该事件可以控制http url到文件路径的映射
 */
public class MKHttpBeforeAccessCallBack implements IMKHttpBeforeAccessCallBack {
    /**
     * 在http文件服务器中,收到http访问文件或目录前的广播,通过该事件可以控制http url到文件路径的映射
     * 在该事件中通过自行覆盖path参数，可以做到譬如根据虚拟主机或者app选择不同http根目录的目的
     *
     * @param parser http请求内容对象
     * @param path   文件绝对路径,覆盖之可以重定向到其他文件
     * @param sender http客户端相关信息
     */
    public void invoke(MK_PARSER parser, String path, MK_SOCK_INFO sender){

    }
}
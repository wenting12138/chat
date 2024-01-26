package com.wen.im.core.utils;

import com.wen.im.core.protocols.ImRequestHeader;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

/**
 * @author wenting
 */
public class ChannelAttrKeyUtils {

    public static final String CLIENT_VERSION = "clientVersion";
    public static final String CLIENT_IDENTIFY = "clientIdentify";
    public static final String CLIENT_UID = "uid";

    public static AttributeKey<String> CLIENT_VERSION_KEY = AttributeKey.valueOf(CLIENT_VERSION);
    public static AttributeKey<String> CLIENT_IDENTIFY_KEY = AttributeKey.valueOf(CLIENT_IDENTIFY);
    public static AttributeKey<String> CLIENT_UID_KEY = AttributeKey.valueOf(CLIENT_UID);

    public static ImRequestHeader parseChannel(Channel ch){
        ImRequestHeader header = new ImRequestHeader();
        header.setUid(ch.attr(CLIENT_UID_KEY).get());
        header.setClientVersion(ch.attr(CLIENT_VERSION_KEY).get());
        header.setClientIdentify(ch.attr(CLIENT_IDENTIFY_KEY).get());
        return header;
    }
}

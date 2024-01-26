package com.wen.im.common.model.dto.msg;

import java.io.Serializable;

public class EmojisMsgDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  下载地址
     */
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
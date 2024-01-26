package com.wen.im.common.model.dto.msg;

import java.io.Serializable;

public class BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     *  大小（字节）
     */
    private Long size;
    /**
     *  下载地址
     */
    private String url;

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
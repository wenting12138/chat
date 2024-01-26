package com.wen.im.common.model.dto.msg;

import java.io.Serializable;

public class VideoMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  缩略图宽度（像素）
     */
    private Integer thumbWidth;

    /**
     *  缩略图高度（像素）
     */
    private Integer thumbHeight;

    /**
     *  缩略图大小（字节）
     */
    private Long thumbSize;

    /**
     *  缩略图下载地址
     */
    private String thumbUrl;

    public Integer getThumbWidth() {
        return thumbWidth;
    }

    public void setThumbWidth(Integer thumbWidth) {
        this.thumbWidth = thumbWidth;
    }

    public Integer getThumbHeight() {
        return thumbHeight;
    }

    public void setThumbHeight(Integer thumbHeight) {
        this.thumbHeight = thumbHeight;
    }

    public Long getThumbSize() {
        return thumbSize;
    }

    public void setThumbSize(Long thumbSize) {
        this.thumbSize = thumbSize;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }
}
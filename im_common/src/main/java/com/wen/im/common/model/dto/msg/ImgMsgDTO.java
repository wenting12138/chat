package com.wen.im.common.model.dto.msg;

import java.io.Serializable;

public class ImgMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  宽度（像素）
     */
    private Integer width;

    /**
     *  高度（像素）
     */
    private Integer height;

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
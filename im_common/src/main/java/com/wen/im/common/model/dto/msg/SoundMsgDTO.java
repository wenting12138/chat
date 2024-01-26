package com.wen.im.common.model.dto.msg;

import java.io.Serializable;

public class SoundMsgDTO extends BaseFileDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *  时长（秒）
     */
    private Integer second;

    public Integer getSecond() {
        return second;
    }

    public void setSecond(Integer second) {
        this.second = second;
    }
}
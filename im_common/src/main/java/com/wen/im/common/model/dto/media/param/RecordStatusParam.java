package com.wen.im.common.model.dto.media.param;

import java.io.Serializable;

/**
 * 录像状态
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
public class RecordStatusParam implements Serializable {

    private static final long serialVersionUID = 1;

    // app
    private String app;

    // 流id
    private String stream;

    // 0为hls，1为mp4,2:hls-fmp4,3:http-fmp4,4:http-ts 当0时需要开启配置分片持久化
    private Integer type;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

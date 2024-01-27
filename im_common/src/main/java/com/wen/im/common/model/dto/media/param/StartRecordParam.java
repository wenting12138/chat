package com.wen.im.common.model.dto.media.param;

import java.io.Serializable;

/**
 * 开始录像
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
public class StartRecordParam implements Serializable {

    private static final long serialVersionUID = 1;

    // app
    private String app;

    // 流id
    private String stream;

    // 0为hls，1为mp4,2:hls-fmp4,3:http-fmp4,4:http-ts 当0时需要开启配置分片持久化
    private Integer type;

    // 录像保存目录
    private String customized_path;

    // mp4录像切片时间大小,单位秒，置0则采用配置项
    private Long max_second = 1L;

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

    public String getCustomized_path() {
        return customized_path;
    }

    public void setCustomized_path(String customized_path) {
        this.customized_path = customized_path;
    }

    public Long getMax_second() {
        return max_second;
    }

    public void setMax_second(Long max_second) {
        this.max_second = max_second;
    }
}

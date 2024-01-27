package com.wen.im.common.model.dto.media.param;

import java.io.Serializable;

/**
 * 关闭流请求参数
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
public class CloseStreamsParam implements Serializable {

    private static final long serialVersionUID = 1;

    // app
    private String app;

    // 流id
    private String stream;

    // 是否强制关闭
    private Integer force=1;

    // 流的协议
    private String schema;

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

    public Integer getForce() {
        return force;
    }

    public void setForce(Integer force) {
        this.force = force;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}

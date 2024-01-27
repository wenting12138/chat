package com.wen.im.common.model.dto.media.param;

import java.io.Serializable;

/**
 * 获取流列表
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
public class GetMediaListParam implements Serializable {

    private static final long serialVersionUID = 1;


    // app
    private String app;

    // 流id
    private String stream;

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

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
}

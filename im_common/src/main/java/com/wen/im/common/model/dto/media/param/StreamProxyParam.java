package com.wen.im.common.model.dto.media.param;


/**
 * 拉流代理参数
 *
 * @author lidaofu
 * @since 2023/11/29
 **/
public class StreamProxyParam {


    // app
    private String app;

    // 流id
    private String stream;

    // 代理流地址
    private String url;

    // rtsp拉流时，拉流方式，0：tcp，1：udp，2：组播
    private Integer rtpType;

    // 拉流重试次数,不传此参数或传值<=0时，则无限重试
    private Integer retryCount;

    // 拉流超时时间，单位秒，float类型
    private Integer timeoutSec;

    // 开启hls转码
    private Integer enableHls;

    // 开启rtsp/webrtc转码
    private Integer enableRtsp;


    // 开启rtmp/flv转码
    private Integer enableRtmp;

    // 开启ts/ws转码
    private Integer enableTs;

    // 转协议是否开启音频
    private Integer enableAudio;

    // 开启转fmp4
    private Integer enableFmp4;

    // 开启mp4录制
    private Integer enableMp4;

    // mp4录制切片大小
    private Integer mp4MaxSecond;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getRtpType() {
        return rtpType;
    }

    public void setRtpType(Integer rtpType) {
        this.rtpType = rtpType;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public Integer getTimeoutSec() {
        return timeoutSec;
    }

    public void setTimeoutSec(Integer timeoutSec) {
        this.timeoutSec = timeoutSec;
    }

    public Integer getEnableHls() {
        return enableHls;
    }

    public void setEnableHls(Integer enableHls) {
        this.enableHls = enableHls;
    }

    public Integer getEnableRtsp() {
        return enableRtsp;
    }

    public void setEnableRtsp(Integer enableRtsp) {
        this.enableRtsp = enableRtsp;
    }

    public Integer getEnableRtmp() {
        return enableRtmp;
    }

    public void setEnableRtmp(Integer enableRtmp) {
        this.enableRtmp = enableRtmp;
    }

    public Integer getEnableTs() {
        return enableTs;
    }

    public void setEnableTs(Integer enableTs) {
        this.enableTs = enableTs;
    }

    public Integer getEnableAudio() {
        return enableAudio;
    }

    public void setEnableAudio(Integer enableAudio) {
        this.enableAudio = enableAudio;
    }

    public Integer getEnableFmp4() {
        return enableFmp4;
    }

    public void setEnableFmp4(Integer enableFmp4) {
        this.enableFmp4 = enableFmp4;
    }

    public Integer getEnableMp4() {
        return enableMp4;
    }

    public void setEnableMp4(Integer enableMp4) {
        this.enableMp4 = enableMp4;
    }

    public Integer getMp4MaxSecond() {
        return mp4MaxSecond;
    }

    public void setMp4MaxSecond(Integer mp4MaxSecond) {
        this.mp4MaxSecond = mp4MaxSecond;
    }
}

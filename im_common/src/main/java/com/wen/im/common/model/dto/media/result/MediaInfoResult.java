package com.wen.im.common.model.dto.media.result;

import java.io.Serializable;
import java.util.List;

/**
 * MediaInfoResult: 流信息
 *
 * @author lidaofu
 * @since 2023/3/30
 **/
public class MediaInfoResult implements Serializable {

    private static final long serialVersionUID = 1;

    // app
    private String app;

    // 流id
    private String stream;

    // 本协议观看人数
    private Integer readerCount;

    // 产生源类型，包括 unknown = 0,rtmp_push=1,rtsp_push=2,rtp_push=3,pull=4,ffmpeg_pull=5,mp4_vod=6,device_chn=7
    private Integer originType;

    // 产生源的url
    private String originUrl;

    // 产生源的url的类型
    private String originTypeStr;

    // 观看总数 包括hls/rtsp/rtmp/http-flv/ws-flv
    private Integer totalReaderCount;

    // schema
    private String schema;

    // 存活时间，单位秒
    private Integer aliveSecond;

    // 数据产生速度，单位byte/s
    private Long bytesSpeed;

    // GMT unix系统时间戳，单位秒
    private Long createStamp;

    // 是否录制Hls
    private Boolean isRecordingHLS;

    // 是否录制mp4
    private Boolean isRecordingMP4;

    // 虚拟地址
    private String vhost;

    private List<Track> tracks;

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

    public Integer getReaderCount() {
        return readerCount;
    }

    public void setReaderCount(Integer readerCount) {
        this.readerCount = readerCount;
    }

    public Integer getOriginType() {
        return originType;
    }

    public void setOriginType(Integer originType) {
        this.originType = originType;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }

    public String getOriginTypeStr() {
        return originTypeStr;
    }

    public void setOriginTypeStr(String originTypeStr) {
        this.originTypeStr = originTypeStr;
    }

    public Integer getTotalReaderCount() {
        return totalReaderCount;
    }

    public void setTotalReaderCount(Integer totalReaderCount) {
        this.totalReaderCount = totalReaderCount;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public Integer getAliveSecond() {
        return aliveSecond;
    }

    public void setAliveSecond(Integer aliveSecond) {
        this.aliveSecond = aliveSecond;
    }

    public Long getBytesSpeed() {
        return bytesSpeed;
    }

    public void setBytesSpeed(Long bytesSpeed) {
        this.bytesSpeed = bytesSpeed;
    }

    public Long getCreateStamp() {
        return createStamp;
    }

    public void setCreateStamp(Long createStamp) {
        this.createStamp = createStamp;
    }

    public Boolean getRecordingHLS() {
        return isRecordingHLS;
    }

    public void setRecordingHLS(Boolean recordingHLS) {
        isRecordingHLS = recordingHLS;
    }

    public Boolean getRecordingMP4() {
        return isRecordingMP4;
    }

    public void setRecordingMP4(Boolean recordingMP4) {
        isRecordingMP4 = recordingMP4;
    }

    public String getVhost() {
        return vhost;
    }

    public void setVhost(String vhost) {
        this.vhost = vhost;
    }

    public List<Track> getTracks() {
        return tracks;
    }

    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}

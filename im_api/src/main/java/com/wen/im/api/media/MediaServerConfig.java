package com.wen.im.api.media;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 媒体服务器配置
 *
 * @author lidaofu
 * @since 2023/11/29
 **/
@Configuration
@ConfigurationProperties(prefix = "media")
public class MediaServerConfig {

    private Integer thread_num;

    private Integer rtmp_port;

    private Integer rtsp_port;

    private Integer http_port;

    private Integer auto_close;

    private Integer streamNoneReaderDelayMS;

    private Integer maxStreamWaitMS;

    private Integer enable_ts;

    private Integer enable_hls;

    private Integer enable_fmp4;

    private Integer enable_rtsp;

    private Integer enable_rtmp;

    private Integer enable_mp4;

    private Integer enable_hls_fmp4;

    private Integer enable_audio;

    private Integer mp4_as_player;

    private Integer mp4_max_second;

    private String mp4_save_path;

    private String hls_save_path;

    private String rootPath;

    private Integer hls_demand;

    private Integer rtsp_demand;

    private Integer rtmp_demand;

    private Integer ts_demand;

    private Integer fmp4_demand;

    private Integer log_level;

    private Integer log_mask;

    private Integer log_file_days;

    private String log_path;

    public Integer getThread_num() {
        return thread_num;
    }

    public void setThread_num(Integer thread_num) {
        this.thread_num = thread_num;
    }

    public Integer getRtmp_port() {
        return rtmp_port;
    }

    public void setRtmp_port(Integer rtmp_port) {
        this.rtmp_port = rtmp_port;
    }

    public Integer getRtsp_port() {
        return rtsp_port;
    }

    public void setRtsp_port(Integer rtsp_port) {
        this.rtsp_port = rtsp_port;
    }

    public Integer getHttp_port() {
        return http_port;
    }

    public void setHttp_port(Integer http_port) {
        this.http_port = http_port;
    }

    public Integer getAuto_close() {
        return auto_close;
    }

    public void setAuto_close(Integer auto_close) {
        this.auto_close = auto_close;
    }

    public Integer getStreamNoneReaderDelayMS() {
        return streamNoneReaderDelayMS;
    }

    public void setStreamNoneReaderDelayMS(Integer streamNoneReaderDelayMS) {
        this.streamNoneReaderDelayMS = streamNoneReaderDelayMS;
    }

    public Integer getMaxStreamWaitMS() {
        return maxStreamWaitMS;
    }

    public void setMaxStreamWaitMS(Integer maxStreamWaitMS) {
        this.maxStreamWaitMS = maxStreamWaitMS;
    }

    public Integer getEnable_ts() {
        return enable_ts;
    }

    public void setEnable_ts(Integer enable_ts) {
        this.enable_ts = enable_ts;
    }

    public Integer getEnable_hls() {
        return enable_hls;
    }

    public void setEnable_hls(Integer enable_hls) {
        this.enable_hls = enable_hls;
    }

    public Integer getEnable_fmp4() {
        return enable_fmp4;
    }

    public void setEnable_fmp4(Integer enable_fmp4) {
        this.enable_fmp4 = enable_fmp4;
    }

    public Integer getEnable_rtsp() {
        return enable_rtsp;
    }

    public void setEnable_rtsp(Integer enable_rtsp) {
        this.enable_rtsp = enable_rtsp;
    }

    public Integer getEnable_rtmp() {
        return enable_rtmp;
    }

    public void setEnable_rtmp(Integer enable_rtmp) {
        this.enable_rtmp = enable_rtmp;
    }

    public Integer getEnable_mp4() {
        return enable_mp4;
    }

    public void setEnable_mp4(Integer enable_mp4) {
        this.enable_mp4 = enable_mp4;
    }

    public Integer getEnable_hls_fmp4() {
        return enable_hls_fmp4;
    }

    public void setEnable_hls_fmp4(Integer enable_hls_fmp4) {
        this.enable_hls_fmp4 = enable_hls_fmp4;
    }

    public Integer getEnable_audio() {
        return enable_audio;
    }

    public void setEnable_audio(Integer enable_audio) {
        this.enable_audio = enable_audio;
    }

    public Integer getMp4_as_player() {
        return mp4_as_player;
    }

    public void setMp4_as_player(Integer mp4_as_player) {
        this.mp4_as_player = mp4_as_player;
    }

    public Integer getMp4_max_second() {
        return mp4_max_second;
    }

    public void setMp4_max_second(Integer mp4_max_second) {
        this.mp4_max_second = mp4_max_second;
    }

    public String getMp4_save_path() {
        return mp4_save_path;
    }

    public void setMp4_save_path(String mp4_save_path) {
        this.mp4_save_path = mp4_save_path;
    }

    public String getHls_save_path() {
        return hls_save_path;
    }

    public void setHls_save_path(String hls_save_path) {
        this.hls_save_path = hls_save_path;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public Integer getHls_demand() {
        return hls_demand;
    }

    public void setHls_demand(Integer hls_demand) {
        this.hls_demand = hls_demand;
    }

    public Integer getRtsp_demand() {
        return rtsp_demand;
    }

    public void setRtsp_demand(Integer rtsp_demand) {
        this.rtsp_demand = rtsp_demand;
    }

    public Integer getRtmp_demand() {
        return rtmp_demand;
    }

    public void setRtmp_demand(Integer rtmp_demand) {
        this.rtmp_demand = rtmp_demand;
    }

    public Integer getTs_demand() {
        return ts_demand;
    }

    public void setTs_demand(Integer ts_demand) {
        this.ts_demand = ts_demand;
    }

    public Integer getFmp4_demand() {
        return fmp4_demand;
    }

    public void setFmp4_demand(Integer fmp4_demand) {
        this.fmp4_demand = fmp4_demand;
    }

    public Integer getLog_level() {
        return log_level;
    }

    public void setLog_level(Integer log_level) {
        this.log_level = log_level;
    }

    public Integer getLog_mask() {
        return log_mask;
    }

    public void setLog_mask(Integer log_mask) {
        this.log_mask = log_mask;
    }

    public Integer getLog_file_days() {
        return log_file_days;
    }

    public void setLog_file_days(Integer log_file_days) {
        this.log_file_days = log_file_days;
    }

    public String getLog_path() {
        return log_path;
    }

    public void setLog_path(String log_path) {
        this.log_path = log_path;
    }
}

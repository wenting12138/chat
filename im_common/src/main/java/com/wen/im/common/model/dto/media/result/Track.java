package com.wen.im.common.model.dto.media.result;

import java.io.Serializable;

public class Track implements Serializable {
    private static final long serialVersionUID = 1;

    private Integer is_video;
    private Integer codec_id;
    private String codec_id_name;
    private Integer codec_type;
    private Integer fps;
    private Integer frames;
    private Integer bit_rate;
    private Integer gop_interval_ms;
    private Integer gop_size;
    private Integer height;
    private Integer key_frames;
    private Integer loss;
    private Boolean ready;
    private Integer width;
    private Integer sample_rate;
    private Integer audio_channel;
    private Integer audio_sample_bit;

    public Integer getIs_video() {
        return is_video;
    }

    public void setIs_video(Integer is_video) {
        this.is_video = is_video;
    }

    public Integer getCodec_id() {
        return codec_id;
    }

    public void setCodec_id(Integer codec_id) {
        this.codec_id = codec_id;
    }

    public String getCodec_id_name() {
        return codec_id_name;
    }

    public void setCodec_id_name(String codec_id_name) {
        this.codec_id_name = codec_id_name;
    }

    public Integer getCodec_type() {
        return codec_type;
    }

    public void setCodec_type(Integer codec_type) {
        this.codec_type = codec_type;
    }

    public Integer getFps() {
        return fps;
    }

    public void setFps(Integer fps) {
        this.fps = fps;
    }

    public Integer getFrames() {
        return frames;
    }

    public void setFrames(Integer frames) {
        this.frames = frames;
    }

    public Integer getBit_rate() {
        return bit_rate;
    }

    public void setBit_rate(Integer bit_rate) {
        this.bit_rate = bit_rate;
    }

    public Integer getGop_interval_ms() {
        return gop_interval_ms;
    }

    public void setGop_interval_ms(Integer gop_interval_ms) {
        this.gop_interval_ms = gop_interval_ms;
    }

    public Integer getGop_size() {
        return gop_size;
    }

    public void setGop_size(Integer gop_size) {
        this.gop_size = gop_size;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getKey_frames() {
        return key_frames;
    }

    public void setKey_frames(Integer key_frames) {
        this.key_frames = key_frames;
    }

    public Integer getLoss() {
        return loss;
    }

    public void setLoss(Integer loss) {
        this.loss = loss;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getSample_rate() {
        return sample_rate;
    }

    public void setSample_rate(Integer sample_rate) {
        this.sample_rate = sample_rate;
    }

    public Integer getAudio_channel() {
        return audio_channel;
    }

    public void setAudio_channel(Integer audio_channel) {
        this.audio_channel = audio_channel;
    }

    public Integer getAudio_sample_bit() {
        return audio_sample_bit;
    }

    public void setAudio_sample_bit(Integer audio_sample_bit) {
        this.audio_sample_bit = audio_sample_bit;
    }
}
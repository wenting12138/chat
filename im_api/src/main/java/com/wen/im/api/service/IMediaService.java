package com.wen.im.api.service;

import com.wen.im.common.model.dto.media.param.*;
import com.wen.im.common.model.dto.media.result.MediaInfoResult;

import java.util.List;

/**
 * 接口服务
 *
 * @author lidaofu
 * @since 2023/11/29
 **/
public interface IMediaService {

    void addStreamProxy(StreamProxyParam param);

    Integer closeStream(CloseStreamParam param);

    Integer closeStreams(CloseStreamsParam param);
    List<MediaInfoResult> getMediaList(GetMediaListParam param);

    Boolean startRecord(StartRecordParam param);

    Boolean stopRecord(StopRecordParam param);

    Boolean isRecording(RecordStatusParam param);
}

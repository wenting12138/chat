package com.wen.im.api.controller.media;

import com.wen.im.api.service.IMediaService;
import com.wen.im.api.vo.response.ApiResponse;
import com.wen.im.common.model.dto.media.param.*;
import com.wen.im.common.model.dto.media.result.MediaInfoResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wenting
 */
@RestController
@RequestMapping("/media")
public class MediaController {
    @Autowired
    private IMediaService mediaService;

    /**
     *  添加rtmp/rtsp拉流代理
     * @param param
     * @return
     */
    @GetMapping("/addStreamProxy")
    public ApiResponse addStreamProxy(StreamProxyParam param) {
        mediaService.addStreamProxy(param);
        return ApiResponse.success();
    }


    /**
     *  关闭流
     * @param param
     * @return
     */
    @GetMapping("/close_stream")
    public ApiResponse closeStream(CloseStreamParam param) {
        Integer status = mediaService.closeStream(param);
        return ApiResponse.success(status);
    }

    /**
     *  关闭流
     * @param param
     * @return
     */
    @GetMapping("/close_streams")
    public ApiResponse closeStreams(CloseStreamsParam param) {
        Integer status = mediaService.closeStreams(param);
        return ApiResponse.success(status);
    }

    /**
     *  获取流列表
     * @param param
     * @return
     */
    @GetMapping("/getMediaList")
    public ApiResponse getMediaList(GetMediaListParam param) {
        List<MediaInfoResult> list = mediaService.getMediaList(param);
        return ApiResponse.success(list);
    }


    /**
     *  开始录像
     * @param param
     * @return
     */
    @PostMapping("/startRecord")
    public ApiResponse startRecord(StartRecordParam param) {
        Boolean flag = mediaService.startRecord(param);
        return ApiResponse.success(flag);
    }


    /**
     * 停止录像
     * @param param
     * @return
     */
    @PostMapping("/stopRecord")
    public ApiResponse stopRecord(StopRecordParam param) {
        Boolean flag = mediaService.stopRecord(param);
        return ApiResponse.success(flag);
    }

    /**
     *  是否录像
     * @param param
     * @return
     */
    @GetMapping("/isRecording")
    public ApiResponse isRecording(RecordStatusParam param) {
        Boolean flag = mediaService.isRecording(param);
        return ApiResponse.success(flag);
    }


}

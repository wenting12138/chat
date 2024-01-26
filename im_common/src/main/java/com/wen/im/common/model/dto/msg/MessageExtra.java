package com.wen.im.common.model.dto.msg;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author wenting
 */
public class MessageExtra implements Serializable {
    private static final long serialVersionUID = 1L;
    //url跳转链接
    private Map<String, UrlInfo> urlContentMap;
    //消息撤回详情
    private MsgRecall recall;
    //艾特的uid
    private List<Long> atUidList;
    //文件消息
    private FileMsgDTO fileMsg;
    //图片消息
    private ImgMsgDTO imgMsgDTO;
    //语音消息
    private SoundMsgDTO soundMsgDTO;
    //文件消息
    private VideoMsgDTO videoMsgDTO;
    /**
     * 表情图片信息
     */
    private EmojisMsgDTO emojisMsgDTO;

    public Map<String, UrlInfo> getUrlContentMap() {
        return urlContentMap;
    }

    public void setUrlContentMap(Map<String, UrlInfo> urlContentMap) {
        this.urlContentMap = urlContentMap;
    }

    public MsgRecall getRecall() {
        return recall;
    }

    public void setRecall(MsgRecall recall) {
        this.recall = recall;
    }

    public List<Long> getAtUidList() {
        return atUidList;
    }

    public void setAtUidList(List<Long> atUidList) {
        this.atUidList = atUidList;
    }

    public FileMsgDTO getFileMsg() {
        return fileMsg;
    }

    public void setFileMsg(FileMsgDTO fileMsg) {
        this.fileMsg = fileMsg;
    }

    public ImgMsgDTO getImgMsgDTO() {
        return imgMsgDTO;
    }

    public void setImgMsgDTO(ImgMsgDTO imgMsgDTO) {
        this.imgMsgDTO = imgMsgDTO;
    }

    public SoundMsgDTO getSoundMsgDTO() {
        return soundMsgDTO;
    }

    public void setSoundMsgDTO(SoundMsgDTO soundMsgDTO) {
        this.soundMsgDTO = soundMsgDTO;
    }

    public VideoMsgDTO getVideoMsgDTO() {
        return videoMsgDTO;
    }

    public void setVideoMsgDTO(VideoMsgDTO videoMsgDTO) {
        this.videoMsgDTO = videoMsgDTO;
    }

    public EmojisMsgDTO getEmojisMsgDTO() {
        return emojisMsgDTO;
    }

    public void setEmojisMsgDTO(EmojisMsgDTO emojisMsgDTO) {
        this.emojisMsgDTO = emojisMsgDTO;
    }
}
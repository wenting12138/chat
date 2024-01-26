package com.wen.im.api.service;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.vo.response.ApiResponse;

/**
 * @author wenting
 */
public interface ChatService {


    public ApiResponse contactPage(int pageSize, String cursor, String uid);

    ApiResponse msgPage(int pageSize, String cursor, Long roomId, Long uid);

    ApiResponse markMsgRead(Long roomId, Long uid);

    ApiResponse msgRead(Integer[] msgIds, Integer uid);

    ApiResponse sendMsg(JSONObject req, Long uid) throws Exception;

    ApiResponse contactDetail(Long contactId, Long uid);

    ApiResponse contactDetailFriend(Long friendUid, Long uid);

    ApiResponse msgMark(JSONObject req, Long uid);

    ApiResponse msgRecall(JSONObject req, Long uid);
}

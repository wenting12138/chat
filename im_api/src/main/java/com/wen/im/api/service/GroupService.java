package com.wen.im.api.service;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.vo.response.ApiResponse;

/**
 * @author wenting
 */
public interface GroupService {


    ApiResponse groupMemeberPage(int pageSize, String cursor, Long roomId, Long uid);

    ApiResponse groupMemberList(Long roomId, Long uid);

    ApiResponse publicGroup(Long id, Long uid);

    Long createGroup(JSONObject req, Long uid) throws Exception;

    ApiResponse addGroupMember(JSONObject req, Long uid);

    ApiResponse addGroupAdmin(JSONObject req, Long uid);

    ApiResponse delGroupAdmin(JSONObject req, Long uid);

    ApiResponse groupExit(JSONObject req, Long uid);

    ApiResponse delGroupMember(JSONObject req, Long uid) throws Exception;

    ApiResponse createMeet(JSONObject req, Long uid);
}

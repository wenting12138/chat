package com.wen.im.api.service;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.vo.request.BatchUserInfoRequest;
import com.wen.im.api.vo.request.LoginByUidRequest;
import com.wen.im.api.vo.response.ApiResponse;
import com.wen.im.common.model.dto.event.NewFriendSessionDTO;
import com.wen.im.common.model.entity.UserBlock;
import com.wen.im.common.model.entity.UserEmoji;

/**
 * @author wenting
 */
public interface UserService {


    ApiResponse login(LoginByUidRequest request) throws Exception;

    ApiResponse userInfo(Long uid);

    ApiResponse batchUserInfo(BatchUserInfoRequest request);

    ApiResponse friendPage(Integer pageSize, String uid);

    ApiResponse friendApplyPage(Integer pageNo, Integer pageSize, Long uid);

    ApiResponse friendApplyUnread(Long uid);

    ApiResponse friendApply(JSONObject req, Long uid) throws Exception;

    ApiResponse updateName(JSONObject req, Long uid);

    ApiResponse applyUserName(JSONObject req, Long uid);

    ApiResponse friendApplyAdd(JSONObject req, Long uid);

    void onOffline();

    ApiResponse updateAvatar(JSONObject req, Long uid);

    ApiResponse addEmoji(UserEmoji userEmoji);

    ApiResponse getEmojiList(Long uid);

    ApiResponse delEmoji(Long id);

    ApiResponse addBlack(UserBlock userBlock, Long uid);
}


package com.wen.im.api.controller.chat;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.mapper.*;
import com.wen.im.api.service.ChatService;
import com.wen.im.common.model.entity.*;
import com.wen.im.api.vo.response.ContactVoResponse;
import com.wen.im.api.vo.response.ApiResponse;
import com.wen.im.api.vo.response.ListCursorResponse;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImRequestHeader;
import com.wen.im.core.server.NettyImServer;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wenting
 */
@RestController
@RequestMapping("/chat")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/public/contact/page")
    public ApiResponse contactPage(@RequestParam("pageSize") int pageSize,
                                   @RequestParam(value = "cursor", required = false) String cursor,
                            @RequestHeader("uid") String uid) {
        return chatService.contactPage(pageSize, cursor, uid);
    }

    @GetMapping("/public/msg/page")
    public ApiResponse msgPage(@RequestParam("pageSize") int pageSize,
                               @RequestParam(value = "cursor", required = false) String cursor,
                               @RequestParam("roomId") Long roomId,
                               @RequestHeader("uid") String uid) {
        Long curUid = 0L;
        if (StringUtils.isNotEmpty(uid) && !"undefined".equals(uid)) {
            curUid = Long.parseLong(uid);
        }
        return chatService.msgPage(pageSize, cursor, roomId, curUid);
    }


    @PutMapping("/msg/read")
    public ApiResponse markMsgRead(@RequestBody JSONObject req,
                               @RequestHeader("uid") Long uid) {
        Long roomId = req.getLong("roomId");
        return chatService.markMsgRead(roomId, uid);
    }

    @GetMapping("/msg/read")
    public ApiResponse msgRead(@RequestParam("msgIds") Integer[] msgIds,
                               @RequestHeader("uid") Integer uid) {
        return chatService.msgRead(msgIds, uid);
    }


    @PostMapping("/msg")
    public ApiResponse sendMsg(@RequestBody JSONObject req,
                               @RequestHeader("uid") Long uid
    ) throws Exception {
        return chatService.sendMsg(req, uid);
    }

    @GetMapping("/public/contact/detail")
    public ApiResponse contactDetail(@RequestParam("id") Long contactId,
                                     @RequestHeader("uid") Long uid) {
        return chatService.contactDetail(contactId, uid);
    }

    @GetMapping("/public/contact/detail/friend")
    public ApiResponse contactDetailFriend(@RequestParam("uid") Long friendUid,
                                           @RequestHeader("uid") Long uid) {
        return chatService.contactDetailFriend(friendUid, uid);
    }


    @PutMapping("/msg/mark")
    public ApiResponse msgMark(@RequestBody JSONObject req,
                               @RequestHeader("uid") Long uid){
        return chatService.msgMark(req, uid);
    }

    @PutMapping("/msg/recall")
    public ApiResponse msgRecall(@RequestBody JSONObject req,
                               @RequestHeader("uid") Long uid){
        return chatService.msgRecall(req, uid);
    }


}

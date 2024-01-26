package com.wen.im.api.controller.user;


import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wen.im.api.mapper.*;
import com.wen.im.api.service.UserService;
import com.wen.im.api.vo.response.*;
import com.wen.im.common.model.entity.*;
import com.wen.im.api.vo.request.BatchUserInfoRequest;
import com.wen.im.api.vo.request.LoginByUidRequest;
import com.wen.im.core.server.NettyImServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wenting
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ApiResponse login(@RequestBody LoginByUidRequest request) throws Exception {
        return userService.login(request);
    }

    @PutMapping("/name")
    public ApiResponse updateName(@RequestBody JSONObject req, @RequestHeader("uid") Long uid) throws Exception {
        return userService.updateName(req, uid);
    }

    @PutMapping("/avatar")
    public ApiResponse updateAvatar(@RequestBody JSONObject req, @RequestHeader("uid") Long uid) throws Exception {
        return userService.updateAvatar(req, uid);
    }


    @GetMapping("/userInfo")
    public ApiResponse userInfo(@RequestHeader("uid") Long uid){
        return userService.userInfo(uid);
    }

    @PostMapping("/public/summary/userInfo/batch")
    public ApiResponse batchUserInfo(@RequestBody BatchUserInfoRequest request) {
        return userService.batchUserInfo(request);
    }

    @GetMapping("/friend/page")
    public ApiResponse friendPage(@RequestParam("pageSize") Integer pageSize,
                                  @RequestHeader("uid") String uid) {
        return userService.friendPage(pageSize, uid);
    }

    @GetMapping("/friend/apply/page")
    public ApiResponse friendApplyPage(
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize,
            @RequestHeader("uid") Long uid) {
        return userService.friendApplyPage(pageNo, pageSize, uid);
    }

    @GetMapping("/friend/apply/unread")
    public ApiResponse friendApplyUnread(@RequestHeader("uid") Long uid) {
        return userService.friendApplyUnread(uid);
    }

    @PutMapping("/friend/apply")
    public ApiResponse friendApply(@RequestBody JSONObject req,
                                   @RequestHeader("uid") Long uid
    ) throws Exception {
        return userService.friendApply(req, uid);
    }

    @PostMapping("/friend/applyUserName")
    public ApiResponse applyUserName(@RequestBody JSONObject req,
                                   @RequestHeader("uid") Long uid
    ) {
        return userService.applyUserName(req, uid);
    }

    @PostMapping("/friend/apply")
    public ApiResponse friendApplyAdd(@RequestBody JSONObject req,
                                   @RequestHeader("uid") Long uid
    ) {
        return userService.friendApplyAdd(req, uid);
    }

    @PostMapping("/emoji")
    public ApiResponse addEmoji(@RequestBody UserEmoji userEmoji){
        return userService.addEmoji(userEmoji);
    }
    @PutMapping("/black")
    public ApiResponse addBlack(@RequestBody UserBlock userBlock, @RequestHeader("uid") Long uid){
        return userService.addBlack(userBlock, uid);
    }

    @DeleteMapping("/emoji")
    public ApiResponse delEmoji(@RequestParam("id") Long id){
        return userService.delEmoji(id);
    }
    @GetMapping("/emoji/list")
    public ApiResponse getEmojiList(@RequestParam("uid") Long uid){
        return userService.getEmojiList(uid);
    }


}

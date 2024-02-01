package com.wen.im.api.controller.group;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.service.GroupService;
import com.wen.im.api.vo.response.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/room")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @GetMapping("/public/group/member/page")
    public ApiResponse groupMemeberPage(@RequestParam("pageSize") int pageSize,
                            @RequestParam(value = "cursor", required = false) String cursor,
                            @RequestParam("roomId") Long roomId,
                            @RequestHeader("uid") String uid
    ) {
        Long curUid = 0L;
        if (StringUtils.isNotEmpty(uid) && !"undefined".equals(uid)) {
            curUid = Long.parseLong(uid);
        }
        return groupService.groupMemeberPage(pageSize, cursor, roomId, curUid);
    }


    @GetMapping("/group/member/list")
    public ApiResponse groupMemberList(@RequestParam("roomId") Long roomId,
                                        @RequestHeader("uid") Long uid) {
        return groupService.groupMemberList(roomId, uid);
    }

    @GetMapping("/public/group")
    public ApiResponse publicGroup(@RequestParam("id") Long id,
                                   @RequestHeader("uid") Long uid) {
        return groupService.publicGroup(id, uid);
    }

    @PostMapping("/group")
    public ApiResponse createGroup(@RequestBody JSONObject req, @RequestHeader("uid") Long uid)throws Exception{
        Long roomId = groupService.createGroup(req, uid);
        return ApiResponse.success(roomId);
    }

    @PostMapping("/group/member")
    public ApiResponse addGroupMember(@RequestBody JSONObject req, @RequestHeader("uid") Long uid)throws Exception{
        return groupService.addGroupMember(req, uid);
    }
    @DeleteMapping("/group/member")
    public ApiResponse delGroupMember(@RequestBody JSONObject req, @RequestHeader("uid") Long uid)throws Exception{
        return groupService.delGroupMember(req, uid);
    }


    @PutMapping("/group/admin")
    public ApiResponse addGroupAdmin(@RequestBody JSONObject req, @RequestHeader("uid") Long uid)throws Exception{
        return groupService.addGroupAdmin(req, uid);
    }
    @DeleteMapping("/group/admin")
    public ApiResponse delGroupAdmin(@RequestBody JSONObject req, @RequestHeader("uid") Long uid)throws Exception{
        return groupService.delGroupAdmin(req, uid);
    }

    @DeleteMapping("/group/member/exit")
    public ApiResponse groupExit(@RequestBody JSONObject req, @RequestHeader("uid") Long uid)throws Exception{
        return groupService.groupExit(req, uid);
    }

    /**
     *  创建会议室
     */
    @PostMapping("/group/createMeet")
    public ApiResponse createMeet(@RequestBody JSONObject req, @RequestHeader("uid") Long uid)throws Exception{
        return groupService.createMeet(req, uid);
    }

}

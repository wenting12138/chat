package com.wen.im.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.mapper.*;
import com.wen.im.api.service.ChatService;
import com.wen.im.api.service.GroupService;
import com.wen.im.api.subsrcibes.springEvent.event.NewFriendSessionEvent;
import com.wen.im.api.vo.response.*;
import com.wen.im.common.enums.GroupMemeberRoleEnum;
import com.wen.im.common.enums.RoomHotFlagEnum;
import com.wen.im.common.enums.RoomTypeEnum;
import com.wen.im.common.model.dto.event.NewFriendSessionDTO;
import com.wen.im.common.model.dto.msg.MsgBuilder;
import com.wen.im.common.model.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wenting
 */
@Service
public class GroupServiceImpl implements GroupService {
    private static final Logger log = LoggerFactory.getLogger(GroupServiceImpl.class);
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private UserApplyMapper userApplyMapper;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ChatService chatService;

    @Override
    public ApiResponse groupMemeberPage(int pageSize, String cursor, Long roomId, Long uid) {
        Long cur = 0L;
        if (cursor != null && cursor.contains("_")) {
            cur = Long.parseLong(cursor.split("_")[1]);
        }else if (cursor != null && !"null".equals(cursor) && !"".equals(cursor)) {
            cur = Long.parseLong(cursor);
        }

        List<GroupMember> groupMemberList = groupMemberMapper.selectGroupMemeberByRoomId(roomId, cur, pageSize);
        List<GroupMemberVoResponse> list = groupMemberList.stream().flatMap(s -> {
            GroupMemberVoResponse response = new GroupMemberVoResponse();
            response.setUid(s.getUid());
            response.setGroupMemberId(s.getId());
            response.setRoleId(s.getRole());
            UserInfo userInfo = userMapper.selectUserByUid(s.getUid());
            response.setName(userInfo.getName());
            response.setAvatar(userInfo.getAvatar());
            response.setActiveStatus(userInfo.getActiveStatus());
            response.setLastOptTime(userInfo.getLastOptTime().getTime());
            return Stream.of(response);
        }).collect(Collectors.toList());

        long lastId = groupMemberMapper.selectMaxId(roomId);
        ListCursorResponse listCursorResponse = new ListCursorResponse();
        listCursorResponse.setList(list);
        if (groupMemberList.size() == 0) {
            listCursorResponse.setLast(true);
        } else {
            long maxId = list.get(list.size() - 1).getGroupMemberId();
            listCursorResponse.setCursor(roomId + "_" + maxId);
            listCursorResponse.setLast(maxId <= lastId);
        }
        return ApiResponse.success(listCursorResponse);
    }

    @Override
    public ApiResponse groupMemberList(Long roomId, Long uid) {
        List<GroupMember> groupMemberList = groupMemberMapper.selectGroupMemeberListByRoomId(roomId);
        List<GroupMemberListResponse> list = groupMemberList.stream().flatMap(s -> {
            GroupMemberListResponse res = buildGroupMemberList(s);
            return Stream.of(res);
        }).collect(Collectors.toList());
        ListCursorResponse listCursorResponse = new ListCursorResponse();
        listCursorResponse.setList(list);
        listCursorResponse.setLast(true);
        return ApiResponse.success(listCursorResponse);
    }

    private GroupMemberListResponse buildGroupMemberList(GroupMember groupMember) {
        GroupMemberListResponse response = new GroupMemberListResponse();
        UserInfo userInfo = userMapper.selectUserByUid(groupMember.getUid());
        response.setAvatar(userInfo.getAvatar());
        response.setName(userInfo.getName());
        response.setLastModifyTime(System.currentTimeMillis());
        response.setUid(groupMember.getUid());
        if (userInfo.getLastOptTime() != null) {
            response.setLastOptTime(userInfo.getLastOptTime().getTime());
        }
        response.setNeedRefresh(false);
        return response;
    }

    @Override
    public ApiResponse publicGroup(Long id, Long uid) {
        Group group = groupMapper.selectGroupByRoomId(id);
        Room room = roomMapper.selectRoomByRoomId(group.getRoomId());
        GroupDetailsResponse response = new GroupDetailsResponse();
        response.setAvatar(group.getAvatar());
        response.setRoomId(group.getRoomId());
        response.setGroupName(group.getName());
        GroupMember member = groupMemberMapper.selectGroupMemberByRoomIdAndUid(group.getId(), uid);
        if (room.getHotFlag() == RoomHotFlagEnum.NONE.getType()) {
            if (member != null) {
                response.setRole(member.getRole());
            }else {
                response.setRole(GroupMemeberRoleEnum.GROUP_EXIT.getRole());
            }
        }else {
            if (member != null) {
                response.setRole(member.getRole());
            }else {
                response.setRole(GroupMemeberRoleEnum.GROUP_MEMBER.getRole());
            }
        }
        response.setOnlineNum(userMapper.onlineNumber(group.getId()));
        return ApiResponse.success(response);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createGroup(JSONObject req, Long uid) throws Exception {
        List<Long> uidList = req.getJSONArray("uidList").toJavaList(Long.class);
        Long roomId = createRoom();
        createGroup(uid, uidList, roomId);
        createContact(roomId, uidList, uid);
        return roomId;
    }

//    private void notifyAllUser(Long roomId, List<Long> uidList, Long uid) throws Exception{
//        ImRequest request = ImRequest.createRequest(RequestCode.ROOM_CHAT, uid);
//        List<Long> allList = new ArrayList<>(uidList);
//        allList.add(uid);
//        NewSessionRequest newSessionRequest = new NewSessionRequest();
//        newSessionRequest.setUid(uid);
//        request.setBody(new RoomChatBody(null, allList));
//        imServer.sendMqMsg(request);
//    }

    private void createContact(Long roomId, List<Long> uidList, Long uid) {
        Contact contact1 = new Contact();
        contact1.setRoomId(roomId);
        contact1.setUid(uid);
        contact1.setCreateTime(new Date());
        contact1.setActiveTime(new Date());
        contactMapper.save(contact1);
        createContactList(uidList, roomId);
    }

    private void createContactList(List<Long> uidList, Long roomId) {
        for (Long u : uidList) {
            Contact contact1 = new Contact();
            contact1.setRoomId(roomId);
            contact1.setUid(u);
            contact1.setCreateTime(new Date());
            contact1.setActiveTime(new Date());
            contactMapper.save(contact1);
        }
    }
    private void createGroupMember(List<Long> uidList, Long groupId) {
        // 普通成员
        uidList = uidList.stream().distinct().collect(Collectors.toList());
        for (Long u : uidList) {
            GroupMember groupMember1 = new GroupMember();
            groupMember1.setUid(u);
            groupMember1.setGroupId(groupId);
            groupMember1.setRole(GroupMemeberRoleEnum.GROUP_MEMBER.getRole());
            groupMember1.setCreateTime(new Date());
            groupMemberMapper.save(groupMember1);
        }
    }


    private void createGroup(Long curUid, List<Long> uidList, Long roomId) {
        UserInfo userInfo = userMapper.selectUserByUid(curUid);
        Group group = new Group();
        group.setAvatar(userInfo.getAvatar());
        group.setRoomId(roomId);
        group.setCreateTime(new Date());
        group.setName(userInfo.getName() + "的群组");
        groupMapper.save(group);
        Long groupId = group.getId();
        createGroupMember(uidList, groupId);

        // 群主成员
        GroupMember groupLeader = new GroupMember();
        groupLeader.setUid(curUid);
        groupLeader.setGroupId(groupId);
        groupLeader.setRole(GroupMemeberRoleEnum.GROUP_LEADER.getRole());
        groupLeader.setCreateTime(new Date());
        groupMemberMapper.save(groupLeader);
    }


    private Long createRoom() {
        Room room = new Room();
        room.setHotFlag(RoomHotFlagEnum.NONE.getType());
        room.setType(RoomTypeEnum.ROOM_CHAT.getType());
        room.setCreateTime(new Date());
        room.setActiveTime(new Date());
        roomMapper.saveRoom(room);
        return room.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse addGroupMember(JSONObject req, Long uid) {
        Long roomId = req.getLong("roomId");
        Room room = roomMapper.selectRoomByRoomId(roomId);
        if (room == null) {
            return ApiResponse.error("房间已不存在", -1);
        }
        if (room.getType() == 2) {
            return ApiResponse.error("单聊房间禁止拉人", -1);
        }
        List<Long> uidList = req.getJSONArray("uidList").toJavaList(Long.class);
        uidList = uidList.stream().distinct().collect(Collectors.toList());
        this.addGroupApply(roomId, uidList, uid);
        return ApiResponse.success();
    }

    private void addGroupApply(Long roomId, List<Long> uidList, Long uid) {
        uidList.forEach(s-> {
            UserApply apply = new UserApply();
            apply.setCreateTime(new Date());
            apply.setMsg("进群邀请");
            apply.setTargetId(s);
            apply.setUid(uid);
            apply.setReadStatus(1);
            JSONObject extra = new JSONObject();
            extra.put("roomId", roomId);
            apply.setExtra(extra.toJSONString());
            apply.setStatus(1);
            apply.setType(2);
            userApplyMapper.save(apply);
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse addGroupAdmin(JSONObject req, Long uid) {
        // {"roomId":10007,"uidList":[10002]}
        Long roomId = req.getLong("roomId");
        List<Long> uidList = req.getJSONArray("uidList").toJavaList(Long.class)
                .stream().distinct().collect(Collectors.toList());
        Group group = groupMapper.selectGroupByRoomId(roomId);
        GroupMember groupMember = groupMemberMapper.selectGroupMemberByRoomIdAndUid(group.getId(), uid);
        if (groupMember == null || groupMember.getRole() != GroupMemeberRoleEnum.GROUP_LEADER.getRole()) {
            return ApiResponse.error("权限不足", -1);
        }
        uidList.forEach(adminUid -> {
            groupMemberMapper.upateRole(adminUid, group.getId(), GroupMemeberRoleEnum.GROUP_MANAGER.getRole());
        });
        return ApiResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse delGroupAdmin(JSONObject req, Long uid) {
        // {"roomId":10007,"uidList":[10002]}
        Long roomId = req.getLong("roomId");
        List<Long> uidList = req.getJSONArray("uidList").toJavaList(Long.class)
                .stream().distinct().collect(Collectors.toList());
        Group group = groupMapper.selectGroupByRoomId(roomId);
        GroupMember groupMember = groupMemberMapper.selectGroupMemberByRoomIdAndUid(group.getId(), uid);
        if (groupMember == null || groupMember.getRole() != GroupMemeberRoleEnum.GROUP_LEADER.getRole()) {
            return ApiResponse.error("权限不足", -1);
        }
        uidList.forEach(adminUid -> {
            groupMemberMapper.upateRole(adminUid, group.getId(), GroupMemeberRoleEnum.GROUP_MEMBER.getRole());
        });
        return ApiResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse groupExit(JSONObject req, Long uid) {
        Long roomId = req.getLong("roomId");
        Group group = groupMapper.selectGroupByRoomId(roomId);
        GroupMember groupMember = groupMemberMapper.selectGroupMemberByRoomIdAndUid(group.getId(), uid);
        if (groupMember == null) {
            return ApiResponse.error("你已不是该群人员", -1);
        }
        if (groupMember.getRole() == GroupMemeberRoleEnum.GROUP_LEADER.getRole()) {
            // 群主退出群聊就是解散群
            groupMemberMapper.removeAll(group.getId());
            groupMapper.delGroup(group.getId());
            contactMapper.removeAll(roomId);
            roomMapper.delRoom(roomId);
        }else {
            // 其他就是自己退出
            groupMemberMapper.delGroup(group.getId(), uid);
            contactMapper.delContact(uid, roomId);
        }
        return ApiResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse delGroupMember(JSONObject req, Long uid) throws Exception {
        Long roomId = req.getLong("roomId");
        Long delUid = req.getLong("uid");
        Group group = groupMapper.selectGroupByRoomId(roomId);
        UserInfo userInfo = userMapper.selectUserByUid(delUid);
        GroupMember groupMember = groupMemberMapper.selectGroupMemberByRoomIdAndUid(group.getId(), uid);
        if (groupMember == null || groupMember.getRole() != GroupMemeberRoleEnum.GROUP_LEADER.getRole()) {
            return ApiResponse.error("权限不足", -1);
        }
        String msg = userInfo.getName() + "被移除了本群";
        chatService.sendMsg(MsgBuilder.buildSysMsg(roomId, msg, new ArrayList<>()), -1L);

        groupMemberMapper.delGroup(group.getId(), delUid);
        contactMapper.delContact(delUid, roomId);
        List<GroupMember> groupMembers = groupMemberMapper.selectGroupMemeberListByRoomId(roomId);
        List<Long> groupAllUidList = groupMembers.stream().map(GroupMember::getUid).collect(Collectors.toList());
        groupAllUidList.add(delUid);
        groupAllUidList = groupAllUidList.stream().distinct().collect(Collectors.toList());
        publisher.publishEvent(new NewFriendSessionEvent(groupAllUidList, buildNewFriendBody(2, roomId, delUid)));
        return ApiResponse.success();
    }

    public NewFriendSessionDTO buildNewFriendBody(Integer changeType, Long roomId, Long uid) {
        UserInfo userInfo = userMapper.selectUserByUid(uid);
        NewFriendSessionDTO dto = new NewFriendSessionDTO();
        dto.setChangeType(changeType);
        dto.setActiveStatus(userInfo.getActiveStatus());
        dto.setLastOptTime(userInfo.getLastOptTime().getTime());
        dto.setUid(uid);
        dto.setRoomId(roomId);
        return dto;
    }

    private Long createMeet() {
        Room room = new Room();
        room.setHotFlag(RoomHotFlagEnum.NONE.getType());
        room.setType(RoomTypeEnum.MEET.getType());
        room.setCreateTime(new Date());
        room.setActiveTime(new Date());
        roomMapper.saveRoom(room);
        return room.getId();
    }

    private void createMeetGroup(Long curUid, Long roomId) {
        UserInfo userInfo = userMapper.selectUserByUid(curUid);
        Group group = new Group();
        group.setAvatar(userInfo.getAvatar());
        group.setRoomId(roomId);
        group.setCreateTime(new Date());
        group.setName(userInfo.getName() + "的会议");
        groupMapper.save(group);
        Long groupId = group.getId();

        // 群主成员
        GroupMember groupLeader = new GroupMember();
        groupLeader.setUid(curUid);
        groupLeader.setGroupId(groupId);
        groupLeader.setRole(GroupMemeberRoleEnum.GROUP_LEADER.getRole());
        groupLeader.setCreateTime(new Date());
        groupMemberMapper.save(groupLeader);
    }


    @Override
    public ApiResponse createMeet(JSONObject req, Long uid) {
        Long meet = createMeet();
        createMeetGroup(uid, meet);
        return ApiResponse.success(meet);
    }
}

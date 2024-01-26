package com.wen.im.api.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wen.im.api.mapper.*;
import com.wen.im.api.service.ChatService;
import com.wen.im.api.service.UserService;
import com.wen.im.api.subsrcibes.springEvent.event.ApplyFriendEvent;
import com.wen.im.api.subsrcibes.springEvent.event.NewFriendSessionEvent;
import com.wen.im.api.vo.request.BatchUserInfoRequest;
import com.wen.im.api.vo.request.LoginByUidRequest;
import com.wen.im.api.vo.response.*;
import com.wen.im.common.enums.GroupMemeberRoleEnum;
import com.wen.im.common.enums.RoomHotFlagEnum;
import com.wen.im.common.enums.RoomTypeEnum;
import com.wen.im.common.enums.UserPowerEnum;
import com.wen.im.common.model.dto.event.NewFriendSessionDTO;
import com.wen.im.common.model.dto.event.OnStatusChangeEvent;
import com.wen.im.common.model.dto.msg.MsgBuilder;
import com.wen.im.common.model.entity.*;
import com.wen.im.common.utils.RandomUserUtil;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImRequestHeader;
import com.wen.im.core.server.NettyImServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wenting
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserApplyMapper userApplyMapper;
    @Autowired
    private UserFriendMapper userFriendMapper;
    @Autowired
    private RoomFriendMapper roomFriendMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private ChatService chatService;
    @Autowired
    private UserEmojiMapper userEmojiMapper;
    @Autowired
    private UserBlockMapper userBlockMapper;


    @Override
    public ApiResponse login(LoginByUidRequest request) throws Exception{
        UserInfo userInfo = userMapper.selectUserByUserName(request.getUserName());
        if (userInfo == null) {
            //
            userInfo = new UserInfo();
            userInfo.setPassword(request.getPassword());
            userInfo.setUserName(request.getUserName());
            userInfo.setActiveStatus(1);
            userInfo.setCreateTime(new Date());
            userInfo.setLastOptTime(new Date());
            userInfo.setSex(1);
            userInfo.setPower(UserPowerEnum.USER.getType());
            userInfo.setName(RandomUserUtil.getRandomUser().getName());
            userMapper.save(userInfo);
        }
        if (!userInfo.getPassword().equals(request.getPassword())) {
            return ApiResponse.error(null, null);
        }
        LoginSuccessUserVoResponse response = new LoginSuccessUserVoResponse();
        response.setUid(userInfo.getUid());
        response.setAvatar(userInfo.getAvatar());
        response.setName(userInfo.getName());
        response.setToken("");
        response.setPower(userInfo.getPower());
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse userInfo(Long uid) {
        UserInfo userInfo = userMapper.selectUserByUid(uid);
        UserDetailsResponse response = new UserDetailsResponse();
        response.setAvatar(userInfo.getAvatar());
        response.setUid(userInfo.getUid());
        response.setSex(userInfo.getSex());
        response.setModifyNameChance(1);
        response.setName(userInfo.getName());
        response.setBadge("");
        response.setPower(userInfo.getPower());
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse batchUserInfo(BatchUserInfoRequest request) {
        List<JSONObject> list = new ArrayList<>();
        for (JSONObject uidObj : request.getReqList()) {
            Long uid = uidObj.getLong("uid");
            UserInfo userInfo = userMapper.selectUserByUid(uid);
            JSONObject item = new JSONObject();
            item.put("uid", uid);
            if (userInfo != null) {
                item.put("name", userInfo.getName());
                item.put("avatar", userInfo.getAvatar());
                item.put("itemIds", new ArrayList<>());
                item.put("locPlace", "");
                item.put("needRefresh", true);
            }
            list.add(item);
        }
        return ApiResponse.success(list);
    }

    @Override
    public ApiResponse friendPage(Integer pageSize, String uid) {
        List<UserFriend> list = userFriendMapper.selectUserFriendByUid(uid, pageSize);
        List<ContactItemResponse> responseList = list.stream().flatMap(s -> {
            ContactItemResponse res = new ContactItemResponse();
            UserInfo userInfo = userMapper.selectUserByUid(s.getFriendUid());
            res.setUid(s.getFriendUid());
            res.setLastOptTime(userInfo.getLastOptTime().getTime());
            res.setActiveStatus(userInfo.getActiveStatus());
            return Stream.of(res);
        }).collect(Collectors.toList());
        ListCursorResponse listCursorResponse = new ListCursorResponse();
        listCursorResponse.setLast(true);
        listCursorResponse.setList(responseList);
        if (responseList.size() > 0) {
            listCursorResponse.setCursor(String.valueOf(list.get(0).getId()));
        }
        return ApiResponse.success(listCursorResponse);
    }

    @Override
    public ApiResponse friendApplyPage(Integer pageNo, Integer pageSize, Long uid) {
        PageHelper.startPage(pageNo, pageSize);
        List<UserApply> userApplyList = userApplyMapper.selectApplyByUid(uid);
        PageInfo<UserApply> pageInfo = new PageInfo<>(userApplyList);
        List<UserApplyVoResponse> responseList = pageInfo.getList().stream().flatMap(s -> {
            UserApplyVoResponse response = new UserApplyVoResponse();
            response.setMsg(s.getMsg());
            response.setApplyId(s.getId());
            response.setStatus(s.getStatus());
            response.setType(s.getType());
            response.setMsg(s.getMsg());
            response.setUid(s.getUid());
            if (s.getType() == 2) {
                JSONObject extra = JSONObject.parseObject(s.getExtra());
                Long roomId = extra.getLong("roomId");
                Group group = groupMapper.selectGroupByRoomId(roomId);
                if (group != null) {
                    response.setRoomId(roomId);
                    response.setAvatar(group.getAvatar());
                    response.setGroupId(group.getId());
                    response.setGroupName(group.getName());
                }
            }
            return Stream.of(response);
        }).collect(Collectors.toList());
        ListPageResponse response = new ListPageResponse();
        response.setPageNo(pageNo);
        response.setPageSize(pageSize);
        response.setTotalRecords(pageInfo.getTotal());
        response.setList(responseList);
        PageHelper.clearPage();
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse friendApplyUnread(Long uid) {
        int unReadCount = userApplyMapper.unreadCount(uid);
        JSONObject res = new JSONObject();
        res.put("unReadCount", unReadCount);
        return ApiResponse.success(res);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse friendApply(JSONObject req, Long uid) throws Exception {
        Long applyId = req.getLong("applyId");
        // 修改申请状态
        updateApplyStatus(applyId);
        UserApply userApply = userApplyMapper.selectApplyById(applyId);
        if (userApply.getType() == 1) {
            // 加入
            Long applyUid = userApply.getUid();
            // 保存 userFriend  成为朋友
            saveFriend(uid, applyUid);
            // 创建房间
            Long roomId = createRoom();
            // 保存 roomFriend
            saveRoomFriend(applyUid, uid, roomId);
            // 保存双向联系人
            saveContact(roomId, applyUid, uid);
            // 保存组
            saveGroup(roomId, applyUid, uid);
            String msg = "我通过了你的好友请求，现在我们可以开始聊天了。";
            chatService.sendMsg(MsgBuilder.buildTextMsg(roomId, msg, new ArrayList<>()), applyUid);
        }else {
            // 入群
            JSONObject extra = JSONObject.parseObject(userApply.getExtra());
            Long roomId = extra.getLong("roomId");
            Group group = groupMapper.selectGroupByRoomId(roomId);
            this.createGroupMember(Arrays.asList(userApply.getTargetId()), group.getId());
            this.createContactList(Arrays.asList(userApply.getTargetId()), roomId);
            List<GroupMember> groupMembers = groupMemberMapper.selectGroupMemeberListByRoomId(roomId);
            List<Long> groupAllUidList = groupMembers.stream().map(GroupMember::getUid).collect(Collectors.toList());
            publisher.publishEvent(new NewFriendSessionEvent(groupAllUidList, buildNewFriendBody(1, roomId, userApply.getTargetId())));
            UserInfo userInfo = userMapper.selectUserByUid(userApply.getTargetId());
            String msg = userInfo.getName() + "加入了本群";
            chatService.sendMsg(MsgBuilder.buildSysMsg(roomId, msg, new ArrayList<>()), -1L);
        }
        return ApiResponse.success();
    }

    public NewFriendSessionDTO buildNewFriendBody(Integer changeType, Long roomId, Long uid) {
        UserInfo userInfo = userMapper.selectUserByUid(uid);
        NewFriendSessionDTO dto = new NewFriendSessionDTO();
        dto.setChangeType(changeType); // 加入群聊
        dto.setActiveStatus(userInfo.getActiveStatus());
        dto.setLastOptTime(userInfo.getLastOptTime().getTime());
        dto.setUid(uid);
        dto.setRoomId(roomId);
        return dto;
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


    private void saveGroup(Long roomId, Long applyUid, Long uid) {
        UserInfo userInfo = userMapper.selectUserByUid(applyUid);
        // group
        Group group = new Group();
        group.setAvatar(userInfo.getAvatar());
        group.setName(userInfo.getName());
        group.setRoomId(roomId);
        group.setCreateTime(new Date());
        groupMapper.save(group);
        // groupmember
        GroupMember groupMember1 = new GroupMember();
        groupMember1.setUid(applyUid);
        groupMember1.setGroupId(group.getId());
        groupMember1.setRole(GroupMemeberRoleEnum.GROUP_LEADER.getRole());
        groupMember1.setCreateTime(new Date());
        groupMemberMapper.save(groupMember1);

        GroupMember groupMember2 = new GroupMember();
        groupMember2.setUid(uid);
        groupMember2.setGroupId(group.getId());
        groupMember2.setRole(GroupMemeberRoleEnum.GROUP_LEADER.getRole());
        groupMember2.setCreateTime(new Date());
        groupMemberMapper.save(groupMember2);
    }

    private void saveContact(Long roomId, Long applyUid, Long uid) {
        Contact contact1 = new Contact();
        contact1.setRoomId(roomId);
        contact1.setUid(applyUid);
        contact1.setCreateTime(new Date());
        contact1.setActiveTime(new Date());
        contactMapper.save(contact1);

        Contact contact2 = new Contact();
        contact2.setRoomId(roomId);
        contact2.setUid(uid);
        contact2.setCreateTime(new Date());
        contact2.setActiveTime(new Date());
        contactMapper.save(contact2);
    }

    private void saveRoomFriend(Long applyUid, Long uid, Long roomId) {
        RoomFriend roomFriend = new RoomFriend();
        roomFriend.setRoomId(roomId);
        if (uid > applyUid) {
            roomFriend.setUid1(applyUid);
            roomFriend.setUid2(uid);
            roomFriend.setRoomKey(applyUid + "_" + uid);
        }else {
            roomFriend.setUid1(uid);
            roomFriend.setUid2(applyUid);
            roomFriend.setRoomKey(uid + "_" + applyUid);
        }
        roomFriend.setStatus(0);
        roomFriend.setCreateTime(new Date());
        roomFriendMapper.save(roomFriend);

    }

    private Long createRoom() {
        Room room = new Room();
        room.setCreateTime(new Date());
        room.setActiveTime(new Date());
        room.setHotFlag(RoomHotFlagEnum.NONE.getType());
        room.setType(RoomTypeEnum.SINGLE_CHAT.getType());
        roomMapper.saveRoom(room);
        return room.getId();
    }

    private void saveFriend(Long uid, Long applyUid) {
        UserFriend friend = new UserFriend();
        friend.setUid(uid);
        friend.setFriendUid(applyUid);
        userFriendMapper.save(friend);
        UserFriend friend2 = new UserFriend();
        friend2.setUid(applyUid);
        friend2.setFriendUid(uid);
        userFriendMapper.save(friend2);
    }

    private void updateApplyStatus(Long applyId) {
        // 修改已读
        userApplyMapper.updateStatus(applyId);
    }

    @Override
    public ApiResponse updateName(JSONObject req, Long uid) {
        String name = req.getString("name");
        userMapper.updateName(uid, name);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse updateAvatar(JSONObject req, Long uid) {
        String avatar = req.getString("avatar");
        userMapper.updateAvatar(uid, avatar);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse applyUserName(JSONObject req, Long uid) {
        String msg = req.getString("msg");
        String targetUserName = req.getString("targetUserName");
        UserInfo userInfo = userMapper.selectUserByUserName(targetUserName);
        if (userInfo == null) {
            return ApiResponse.success();
        }
        UserApply apply = new UserApply();
        apply.setCreateTime(new Date());
        apply.setMsg(msg);
        apply.setTargetId(userInfo.getId());
        apply.setUid(uid);
        apply.setReadStatus(1);
        apply.setStatus(1);
        apply.setType(1);
        userApplyMapper.save(apply);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse friendApplyAdd(JSONObject req, Long uid) {
        // {"msg":"2222","targetUid":10003}
        String msg = req.getString("msg");
        Long targetUid = req.getLong("targetUid");
        UserInfo userInfo = userMapper.selectUserByUid(targetUid);
        if (userInfo == null) {
            return ApiResponse.success();
        }
        UserApply apply = new UserApply();
        apply.setCreateTime(new Date());
        apply.setMsg(msg);
        apply.setTargetId(targetUid);
        apply.setUid(uid);
        apply.setReadStatus(1);
        apply.setStatus(1);
        apply.setType(1);
        userApplyMapper.save(apply);
        int unreadCount = userApplyMapper.unreadCount(targetUid);
        publisher.publishEvent(new ApplyFriendEvent(apply, unreadCount));
        return ApiResponse.success();
    }

    @Override
    public void onOffline() {
//        OnStatusChangeEvent event = new OnStatusChangeEvent();
//        event.setOnlineNum(userMapper.onlineNumberAll());
//        nettyImServer.sendMqMsg(event);
    }

    @Override
    public ApiResponse addEmoji(UserEmoji userEmoji) {
        userEmojiMapper.saveEmoji(userEmoji);
        return ApiResponse.success();
    }

    @Override
    public ApiResponse getEmojiList(Long uid) {
        List<UserEmoji> emojiList = userEmojiMapper.getEmojiList(uid);
        return ApiResponse.success(emojiList);
    }

    @Override
    public ApiResponse delEmoji(Long id) {
        UserEmoji userEmoji = userEmojiMapper.getEmoji(id);
        userEmojiMapper.delEmoji(id);
        return ApiResponse.success(userEmoji);
    }
    @Override
    public ApiResponse addBlack(UserBlock userBlock, Long uid) {
        userBlock.setUid(uid);
        userBlockMapper.saveBlock(userBlock);
        return ApiResponse.success();
    }
}

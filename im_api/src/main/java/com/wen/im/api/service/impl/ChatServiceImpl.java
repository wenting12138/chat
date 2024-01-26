package com.wen.im.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wen.im.api.mapper.*;
import com.wen.im.api.service.ChatService;
import com.wen.im.api.subsrcibes.springEvent.event.SendMsgEvent;
import com.wen.im.api.vo.response.ApiResponse;
import com.wen.im.api.vo.response.ContactVoResponse;
import com.wen.im.api.vo.response.ListCursorResponse;
import com.wen.im.common.enums.GroupMemeberRoleEnum;
import com.wen.im.common.enums.MsgTypeEnum;
import com.wen.im.common.enums.RoomHotFlagEnum;
import com.wen.im.common.model.dto.msg.*;
import com.wen.im.common.model.entity.*;
import com.wen.im.common.utils.PrioritizedUrlDiscover;
import com.wen.im.common.utils.RequestCode;
import com.wen.im.core.protocols.ImRequest;
import com.wen.im.core.protocols.ImRequestHeader;
import com.wen.im.core.server.NettyImServer;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author wenting
 */
@Service
public class ChatServiceImpl implements ChatService {
    private static final Logger log = LoggerFactory.getLogger(ChatServiceImpl.class);
    @Autowired
    private ContactMapper contactMapper;
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoomMapper roomMapper;
    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private GroupMemberMapper groupMemberMapper;
    @Autowired
    private RoomFriendMapper roomFriendMapper;
    @Autowired
    private MessageMarkMapper messageMarkMapper;
    @Autowired
    private ApplicationEventPublisher publisher;
    private static final PrioritizedUrlDiscover discover = new PrioritizedUrlDiscover();

    @Override
    public ApiResponse contactPage(int pageSize, String cursor, String uid) {
        List<Contact> contactList = contactMapper.selectContactByUid(uid);
        List<Room> hotList = roomMapper.selectHotRoom();
        hotList.forEach(s -> {
            Contact contact = new Contact();
            contact.setRoomId(s.getId());
            contact.setUid(Long.parseLong(uid));
            contact.setActiveTime(s.getActiveTime());
            contact.setLastMsgId(s.getLastMsgId());
            contact.setCreateTime(s.getCreateTime());
            contact.setReadTime(s.getActiveTime());
            contactList.add(0, contact);
        });
        List<ContactVoResponse> voResponse = contactList.stream().flatMap(s -> {
            ContactVoResponse response = buildContactResponse(s);
            return Stream.of(response);
        }).collect(Collectors.toList());
        ListCursorResponse listCursorResponse = new ListCursorResponse();
        listCursorResponse.setList(voResponse);
        listCursorResponse.setLast(true);
        return ApiResponse.success(listCursorResponse);
    }

    private ContactVoResponse buildContactResponse(Contact s){
        ContactVoResponse response = new ContactVoResponse();
        int unreadCount = messageMapper.selectUnReadCount(s.getRoomId(), s.getReadTime());
        Room room = roomMapper.selectRoomByRoomId(s.getRoomId());
        Group group = groupMapper.selectGroupByRoomId(s.getRoomId());
        if (s.getActiveTime() != null) {
            response.setActiveTime(s.getActiveTime().getTime());
        }
        response.setUnreadCount(unreadCount);
        response.setRoomId(s.getRoomId());
        response.setHot_Flag(room.getHotFlag());
        if (s.getCreateTime() != null) {
            response.setCreateTime(s.getCreateTime().getTime());
        }
        response.setType(room.getType());
        if (room.getType() == 1) {
            // 群聊
            response.setAvatar(group.getAvatar());
            response.setName(group.getName());
            if (room.getLastMsgId() != null) {
                Message message = messageMapper.selectMessageByMsgId(room.getLastMsgId());
                response.setText(message.getContent());
            }
        }else {
            // 单聊
            Long friendUid = findFriendUid(room, s.getUid());
            UserInfo userInfo = userMapper.selectUserByUid(friendUid);
            response.setAvatar(userInfo.getAvatar());
            response.setName(userInfo.getName());
            if (s.getLastMsgId() != null) {
                Message message = messageMapper.selectMessageByMsgId(s.getLastMsgId());
                response.setText(message.getContent());
            }
        }
        return response;
    }

    @Override
    public ApiResponse msgPage(int pageSize, String cursor, Long roomId, Long uid) {
        Long cur = 0L;
        if (cursor != null && !"null".equals(cursor) && !"".equals(cursor)) {
            cur = Long.parseLong(cursor);
        }
        ListCursorResponse listCursorResponse = new ListCursorResponse();
        Room room = roomMapper.selectRoomByRoomId(roomId);
        if (room == null) {
            return ApiResponse.success(listCursorResponse);
        }
        Comparator<Message> comparing = Comparator.comparing(Message::getId);
        List<Message> msgList = messageMapper.selectMessageByRoomId(roomId, uid, room.getType(), cur, pageSize)
                .stream().sorted(comparing).collect(Collectors.toList());
        List<JSONObject> list = msgList.stream().flatMap(s -> {
            JSONObject msg = transferMessage(s, uid);
            return Stream.of(msg);
        }).collect(Collectors.toList());
        Integer firstId = messageMapper.selectMinId(roomId);
        listCursorResponse.setList(list);
        if (msgList.size() == 0) {
            listCursorResponse.setLast(true);
        } else {
            Long id = msgList.get(0).getId();
            listCursorResponse.setLast(id <= firstId);
            listCursorResponse.setCursor(String.valueOf(id));
        }
        return ApiResponse.success(listCursorResponse);
    }

    private JSONObject transferMessage(Message s, Long curUid){
        JSONObject msg = new JSONObject();
        JSONObject fromUser = new JSONObject();
        fromUser.put("uid", s.getFromUid());
        Room room = roomMapper.selectRoomByRoomId(s.getRoomId());
        msg.put("fromUser", fromUser);
        msg.put("type", room.getType());
        JSONObject message = new JSONObject();

        message.put("body", buildMessageResponseBody(s));

        message.put("id", s.getId());
        message.put("roomId", s.getRoomId());
        message.put("sendTime", s.getCreateTime().getTime());
        message.put("type", s.getType());
        Map<String, Object> messageMark =  messageMarkMapper.markCount(s.getId(), curUid);
        message.put("messageMark", messageMark);
        msg.put("message", message);
        return msg;
    }

    private List<Long> findRoomUidList(Room room) {
        List<Long> uidList = groupMapper.selectUidListByRoomId(room.getId());
        return uidList;
    }

    private Long findFriendUid(Room room, Long curUid) {
        // 单聊
        RoomFriend roomFriend = roomFriendMapper.selectRoomFriendByRoomIdAndUi1(room.getId(), curUid);
        Long friendUid = null;
        if (roomFriend.getUid1().equals(curUid)) {
            friendUid = roomFriend.getUid2();
        }else {
            friendUid = roomFriend.getUid1();
        }
        return friendUid;
    }

    private Object buildMessageResponseBody(Message s) {
        MessageExtra extra = JSON.parseObject(s.getExtra(), MessageExtra.class);
        if (MsgTypeEnum.TEXT.getType() == s.getType()) {
            JSONObject contentJSON = new JSONObject();
            contentJSON.put("content", s.getContent());
            contentJSON.put("atUidList", extra.getAtUidList());
            Long replyMsgId = s.getReplyMsgId();
            Message replyMessage = messageMapper.selectMessageByMsgId(replyMsgId);
            contentJSON.put("reply", buildReplyMsg(replyMessage));
            contentJSON.put("urlContentMap", extra.getUrlContentMap());
            return contentJSON;
        }
        return buildMsgContent(s);
    }

    private Object buildMsgContent(Message msg) {
        MessageExtra extra = JSON.parseObject(msg.getExtra(), MessageExtra.class);
        if (MsgTypeEnum.TEXT.getType() == msg.getType()) {
            return msg.getContent();
        }
        if (MsgTypeEnum.FILE.getType() == msg.getType()) {
            return extra.getFileMsg();
        }
        if (MsgTypeEnum.SYSTEM.getType() == msg.getType()) {
            return msg.getContent();
        }
        if (MsgTypeEnum.IMAGE.getType() == msg.getType()) {
            return extra.getImgMsgDTO();
        }
        if (MsgTypeEnum.EMOJI.getType() == msg.getType()) {
            return extra.getEmojisMsgDTO();
        }
        if (MsgTypeEnum.VOICE.getType() == msg.getType()) {
            return extra.getSoundMsgDTO();
        }
        if (MsgTypeEnum.VIDEO.getType() == msg.getType()) {
            return extra.getVideoMsgDTO();
        }
        if (MsgTypeEnum.UNKNOWN.getType() == msg.getType()) {
            return msg.getContent();
        }
        if (MsgTypeEnum.RECALL.getType() == msg.getType()) {
            UserInfo userInfo = userMapper.selectUserByUid(msg.getFromUid());
            return "\""+ userInfo.getName() +"\"撤回了一条消息";
        }
        return msg.getContent();
    }

    private Object buildReplyMsg(Message replyMessage) {
        if (replyMessage == null) {
            return new HashMap<>();
        }
        UserInfo userInfo = userMapper.selectUserByUid(replyMessage.getFromUid());
        JSONObject reply = new JSONObject();
        reply.put("id", replyMessage.getId());
        reply.put("username", userInfo.getName());
        reply.put("type", replyMessage.getType());
        /** 根据不同类型回复的消息展示也不同-`过渡版` */
        reply.put("body", buildMsgContent(replyMessage));
        /**
         * 是否可消息跳转
         * @enum {number}  `0`否 `1`是
         */
        reply.put("canCallback", replyMessage.getGapCount() > 0 ? 1 : 0);
        /** 跳转间隔的消息条数  */
        reply.put("gapCount", replyMessage.getGapCount());
        return reply;
    }

    @Override
    public ApiResponse markMsgRead(Long roomId, Long uid) {
        Room room = roomMapper.selectRoomByRoomId(roomId);
        if (room.getType() == 1) {
            contactMapper.updateRead(roomId, uid);
        }else {
            roomMapper.updateRead(roomId);
        }
        return ApiResponse.success(null);
    }

    @Override
    public ApiResponse msgRead(Integer[] msgIds, Integer uid) {
        JSONArray arr = new JSONArray();
        for (Integer msgId : msgIds) {
            JSONObject item = new JSONObject();
            item.put("msgId", msgId);
            item.put("readCount", 0);
            item.put("unReadCount", 1);
            arr.add(item);
        }
        return ApiResponse.success(arr);
    }

    @Override
    public ApiResponse sendMsg(JSONObject req, Long uid) throws Exception{
        Long roomId = req.getLong("roomId");
        Room room = roomMapper.selectRoomByRoomId(roomId);
        if (room == null) {
            return ApiResponse.success();
        }
        if (room.getHotFlag() == RoomHotFlagEnum.NONE.getType()) {
            Group group = groupMapper.selectGroupByRoomId(roomId);
            if (group == null) {
                return ApiResponse.success();
            }
            GroupMember groupMember = groupMemberMapper.selectGroupMemberByRoomIdAndUid(group.getId(), uid);
            if (groupMember == null || groupMember.getRole() == GroupMemeberRoleEnum.GROUP_EXIT.getRole()) {
                return ApiResponse.error("你已经不在此群, 不能发送消息", -1);
            }
        }
        Integer msgType = req.getInteger("msgType");
        String content = req.getJSONObject("body").getString("content");
        String gapCount = req.getJSONObject("body").getString("gapCount");
        Long replyMsgId = req.getJSONObject("body").getLong("replyMsgId");

        Message message = new Message();
        message.setContent(content);
        message.setFromUid(uid);
        message.setType(msgType);
        message.setCreateTime(new Date());
        message.setRoomId(roomId);
        message.setStatus(0);
        message.setExtra(buildExtra(req, msgType));
        if (StringUtils.isNotEmpty(gapCount)) {
            message.setGapCount(Integer.parseInt(gapCount));
        } else {
            message.setGapCount(0);
        }
        message.setReplyMsgId(replyMsgId);
        messageMapper.saveMsg(message);
        Long msgId = message.getId();
        if (room.getType() == 1) {
            // 群聊
            roomMapper.updateLastMsgId(msgId, roomId);
        }else {
            // 单聊
            updateContactLastMsgId(msgId, room, uid);
        }
        JSONObject msg = transferMessage(message, uid);
        // 发送事件
        publisher.publishEvent(new SendMsgEvent(msg, room, findSendUid(room, uid), uid));
        return ApiResponse.success(msg);
    }

    private List<Long> findSendUid(Room room, Long uid) {
        List<Long> uidListAll = new ArrayList<>();
        if (room.getType() == 2) {
            // 需要查出对方uid
            Long friendUid = findFriendUid(room, uid);
            uidListAll.add(friendUid);
        }else {
            // 需要查出所有uid: ??拉黑功能可能需要排除拉黑的人
            List<Long> uidList = findRoomUidList(room);
            uidList.remove(uid);
            uidListAll.addAll(uidList);
        }
        return uidListAll;
    }

    private void updateContactLastMsgId(Long msgId, Room room, Long uid) {
        Long friendUid = findFriendUid(room, uid);
        contactMapper.updateLastMsgId(msgId, uid, room.getId());
        contactMapper.updateLastMsgId(msgId, friendUid, room.getId());
    }

    private String buildExtra(JSONObject req, Integer msgType) {
        JSONObject body = req.getJSONObject("body");
        MessageExtra extra = new MessageExtra();
        if (body.containsKey("atUidList")) {
            List<Long> atUidList = body.getJSONArray("atUidList").toJavaList(Long.class);
            extra.setAtUidList(atUidList);
        }
        Map<String, UrlInfo> urlInfoMap = discover.getUrlContentMap(body.getString("content"));
        extra.setUrlContentMap(urlInfoMap);
        if (MsgTypeEnum.IMAGE.getType() == msgType) {
            ImgMsgDTO imgMsgDTO = JSON.parseObject(body.toJSONString(), ImgMsgDTO.class);
            extra.setImgMsgDTO(imgMsgDTO);
        }
        if (MsgTypeEnum.VOICE.getType() == msgType) {
            SoundMsgDTO soundMsgDTO = JSON.parseObject(body.toJSONString(), SoundMsgDTO.class);
            extra.setSoundMsgDTO(soundMsgDTO);
        }
        if (MsgTypeEnum.VIDEO.getType() == msgType) {
            VideoMsgDTO videoMsgDTO = JSON.parseObject(body.toJSONString(), VideoMsgDTO.class);
            extra.setVideoMsgDTO(videoMsgDTO);
        }
        if (MsgTypeEnum.EMOJI.getType() == msgType) {
            EmojisMsgDTO emojisMsgDTO = JSON.parseObject(body.toJSONString(), EmojisMsgDTO.class);
            extra.setEmojisMsgDTO(emojisMsgDTO);
        }
        if (MsgTypeEnum.FILE.getType() == msgType) {
            FileMsgDTO fileMsgDTO = JSON.parseObject(body.toJSONString(), FileMsgDTO.class);
            extra.setFileMsg(fileMsgDTO);
        }
        return JSON.toJSONString(extra);
    }

    private Map<String, UrlInfo> buildUrlMap(JSONObject urlContentMap) {
        if (urlContentMap == null || urlContentMap.size() == 0) {
            return new HashMap<>();
        }
        Map<String, UrlInfo> urlInfoMap = new HashMap<>();
        for (String url : urlContentMap.keySet()) {
            urlInfoMap.put(url, urlContentMap.getObject(url, UrlInfo.class));
        }
        return urlInfoMap;
    }

    @Override
    public ApiResponse contactDetail(Long contactId, Long uid) {
        Contact contact = contactMapper.selectContactByContactId(contactId);
        ContactVoResponse response = buildContactResponse(contact);
        return ApiResponse.success(response);
    }

    @Override
    public ApiResponse contactDetailFriend(Long friendUid, Long uid) {
        RoomFriend roomFriend = null;
        if (friendUid > uid) {
            roomFriend = roomFriendMapper.selectRoomFriend(uid, friendUid);
        } else {
            roomFriend = roomFriendMapper.selectRoomFriend(friendUid, uid);
        }
        if (roomFriend == null || roomFriend.getStatus() == 1) {
            return ApiResponse.error("不是好友", -1);
        }
        Contact contact = contactMapper.selectContactByRoomIdAndUid(friendUid, roomFriend.getRoomId());
        ContactVoResponse response = buildContactResponse(contact);
        return ApiResponse.success(response);
    }


    @Override
    public ApiResponse msgMark(JSONObject req, Long uid) {
        // actType	动作类型 1确认 2取消
        Integer actType = req.getInteger("actType");
        // 标记类型 1点赞 2举报
        Integer markType = req.getInteger("markType");
        // 消息 ID
        Long msgId = req.getLong("msgId");

        Integer status = actType == 1 ? 0 : 1;
        MessageMark messageMark = messageMarkMapper.select(msgId, uid);
        if (messageMark == null) {
            messageMark = new MessageMark();
            messageMark.setMsgId(msgId);
            messageMark.setStatus(status);
            messageMark.setType(markType);
            messageMark.setUid(uid);
            messageMark.setCreateTime(new Date());
            messageMarkMapper.save(messageMark);
        }else {
            messageMarkMapper.updateById(messageMark.getId(), status, markType);
        }
        return ApiResponse.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResponse msgRecall(JSONObject req, Long uid) {
        Long msgId = req.getLong("msgId");
        Long roomId = req.getLong("roomId");
        Message message = messageMapper.selectMessageByMsgId(msgId);
        MessageExtra extra = JSON.parseObject(message.getExtra(), MessageExtra.class);
        extra.setRecall(new MsgRecall(uid, new Date()));
        Message update = new Message();
        update.setId(message.getId());
        update.setType(MsgTypeEnum.RECALL.getType());
        update.setExtra(JSON.toJSONString(extra));
        messageMapper.updateById(update);

        return ApiResponse.success();
    }
}

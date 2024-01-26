package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.GroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wenting
 */
@Mapper
@Repository
public interface GroupMemberMapper {


    List<GroupMember> selectGroupMemeberByRoomId(@Param("roomId") Long roomId,
                                                 @Param("cursor") Long cursor,
                                                 @Param("pageSize") Integer pageSize
    );

    List<GroupMember> selectGroupMemeberListByRoomId(@Param("roomId") Long roomId
    );

    long selectMaxId(@Param("roomId") Long roomId);

    void save(GroupMember groupMember);

    GroupMember selectGroupMemberByRoomIdAndUid(@Param("groupId") Long groupId, @Param("uid") Long uid);

    void upateRole(@Param("uid") Long uid, @Param("groupId") Long id, @Param("role") Integer role);

    void delGroup(@Param("groupId") Long id, @Param("uid") Long uid);

    void removeAll(@Param("groupId") Long id);
}

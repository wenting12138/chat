package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.Room;
import com.wen.im.common.model.entity.RoomFriend;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @author wenting
 */
@Mapper
@Repository
public interface RoomFriendMapper {

    RoomFriend selectRoomFriend(@Param("uid1") Long uid1,
                                @Param("uid2") Long uid2
    );

    RoomFriend selectRoomFriendByRoomIdAndUi1(@Param("roomId") Long roomId,
                                @Param("uid") Long uid
    );


    void save(RoomFriend roomFriend);

}

package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.Group;
import com.wen.im.common.model.entity.Room;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wenting
 */
@Mapper
@Repository
public interface RoomMapper {

    Room selectRoomByRoomId(@Param("roomId") Long roomId);

    void saveRoom(Room room);

    void updateRead(@Param("roomId") Long roomId);

    void updateLastMsgId(@Param("msgId") Long msgId, @Param("roomId") Long roomId);

    void delRoom(@Param("roomId") Long roomId);

    List<Room> selectHotRoom();

}

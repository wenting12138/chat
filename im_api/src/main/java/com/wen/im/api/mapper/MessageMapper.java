package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @author wenting
 */
@Mapper
@Repository
public interface MessageMapper {


    List<Message> selectMessageByRoomId(@Param("roomId") Long roomId,
                                        @Param("uid") Long uid,
                                        @Param("type") Integer type,
                                        @Param("cursor") Long cursor,
                                        @Param("pageSize") Integer pageSize
    );

    Integer selectMaxId(@Param("roomId") Long roomId);
    Integer selectMinId(@Param("roomId") Long roomId);

    void saveMsg(Message message);

    Message selectMessageByMsgId(Long msgId);

    void updateRead(@Param("roomId") Long roomId, @Param("uid") Long uid, @Param("type") Integer type);

    int selectUnReadCount(@Param("roomId") Long roomId, @Param("readTime") Date readTime);

    void updateById(Message update);

}

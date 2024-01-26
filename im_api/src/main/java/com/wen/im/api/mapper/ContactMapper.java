package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.Contact;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wenting
 */
@Mapper
@Repository
public interface ContactMapper {

    List<Contact> selectContactByUid(@Param("uid") String uid);

    Contact selectContactByContactId(@Param("contactId") Long contactId);

    Contact selectContactByRoomIdAndUid(@Param("uid") Long friendUid, @Param("roomId") Long roomId);

    void save(Contact contact);

    void updateRead(@Param("roomId") Long roomId, @Param("uid") Long uid);

    void updateLastMsgId(@Param("msgId") Long msgId,
                         @Param("uid") Long uid,
                         @Param("roomId") Long roomId
    );

    void delContact(@Param("uid") Long uid, @Param("roomId") Long roomId);

    void removeAll(@Param("roomId") Long roomId);
}

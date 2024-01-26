package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.Group;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wenting
 */
@Mapper
@Repository
public interface GroupMapper {

    Group selectGroupByRoomId(@Param("roomId") Long roomId);

    void save(Group group);

    List<Long> selectUidListByRoomId(@Param("roomId") Long roomId);

    void delGroup(@Param("groupId") Long id);
}

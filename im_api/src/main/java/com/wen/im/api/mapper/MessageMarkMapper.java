package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.Message;
import com.wen.im.common.model.entity.MessageMark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wenting
 */
@Mapper
@Repository
public interface MessageMarkMapper {

    void save(MessageMark messageMark);

    Map<String, Object> markCount(@Param("msgId") Long msgId, @Param("curUid") Long curUid);

    MessageMark select(@Param("msgId") Long msgId, @Param("uid") Long uid);

    void updateById(@Param("id") Long id, @Param("status") Integer status, @Param("markType") Integer markType);

}

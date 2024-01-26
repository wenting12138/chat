package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.UserApply;
import com.wen.im.common.model.entity.UserFriend;
import com.wen.im.common.model.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author wenting
 */
@Mapper
@Repository
public interface UserApplyMapper {
    List<UserApply> selectApplyByUid(@Param("uid") Long uid);

    int unreadCount(@Param("uid") Long uid);

    void updateStatus(@Param("applyId") Long applyId);
    UserApply selectApplyById(@Param("applyId") Long applyId);

    void save(UserApply apply);
}

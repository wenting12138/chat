package com.wen.im.api.mapper;

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
public interface UserFriendMapper {
    List<UserFriend> selectUserFriendByUid(@Param("uid") String uid, @Param("pageSize") Integer pageSize);

    void save(UserFriend friend);
}

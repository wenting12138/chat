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
public interface UserMapper {
    UserInfo selectUserByUserName(@Param("userName") String userName);

    void online(@Param("uid") Long uid);
    void offline(@Param("uid") Long uid);

    Integer onlineNumber(@Param("groupId") Long groupId);

    void save(UserInfo userInfo);

    UserInfo selectUserByUid(@Param("uid") Long uid);

    void updateName(@Param("uid") Long uid, @Param("name") String name);

    Integer onlineNumberAll();

    void updateAvatar(@Param("uid") Long uid, @Param("avatar") String avatar);
}

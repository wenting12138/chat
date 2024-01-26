package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.UserEmoji;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserEmojiMapper {
    void saveEmoji(UserEmoji userEmoji);
    List<UserEmoji> getEmojiList(@Param("uid") Long uid);
    void delEmoji(@Param("id") Long id);

    UserEmoji getEmoji(@Param("id") Long id);
}

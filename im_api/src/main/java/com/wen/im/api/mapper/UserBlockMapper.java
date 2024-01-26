package com.wen.im.api.mapper;

import com.wen.im.common.model.entity.UserBlock;
import com.wen.im.common.model.entity.UserEmoji;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserBlockMapper {
    void saveBlock(UserBlock userBlock);
    List<UserBlock> getBlockList(@Param("uid") Long uid);
    void delBlock(@Param("id") Long id);
    UserBlock getBlock(@Param("id") Long id);
}

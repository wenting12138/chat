package com.wen.im.api.vo.response;

import java.io.Serializable;
import java.util.List;

/**
 * @author wenting
 */
public class GroupMemberListResponse implements Serializable {
    /** 是否需要更新数据源。 */
    private boolean needRefresh;
    /** 最后更新时间 更新超过 10 分钟异步去更新。 */
    private Long lastModifyTime;
    /** 获得的徽章 */
    private List<Long> itemIds;
    /** 佩戴的徽章 */
    private Long wearingItemId;
    /** 归属地 */
    private String locPlace;
    /** 头像 */
    private String avatar;
    /** 最后一次上下线时间 */
    private Long lastOptTime;
    /** 用户名称 */
    private String name;
    /** uid */
    private Long uid;

    public boolean isNeedRefresh() {
        return needRefresh;
    }

    public void setNeedRefresh(boolean needRefresh) {
        this.needRefresh = needRefresh;
    }

    public Long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public List<Long> getItemIds() {
        return itemIds;
    }

    public void setItemIds(List<Long> itemIds) {
        this.itemIds = itemIds;
    }

    public Long getWearingItemId() {
        return wearingItemId;
    }

    public void setWearingItemId(Long wearingItemId) {
        this.wearingItemId = wearingItemId;
    }

    public String getLocPlace() {
        return locPlace;
    }

    public void setLocPlace(String locPlace) {
        this.locPlace = locPlace;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Long getLastOptTime() {
        return lastOptTime;
    }

    public void setLastOptTime(Long lastOptTime) {
        this.lastOptTime = lastOptTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
}

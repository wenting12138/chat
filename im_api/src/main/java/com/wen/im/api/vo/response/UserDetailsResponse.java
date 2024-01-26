package com.wen.im.api.vo.response;

import java.io.Serializable;

/**
 * @author wenting
 */
public class UserDetailsResponse implements Serializable {
    /** 用户唯一标识 */
    private Long uid;
    /** 用户头像 */
    private String avatar;
    /** 用户名 */
    private String name;
    /** 剩余改名次数 */
    private Integer modifyNameChance;
    /** 性别 1为男性，2为女性 */
    private Integer sex;
    /** 徽章，本地字段，有值用本地，无值用远端 */
    private String badge;
    /** 权限 */
    private Integer power;

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getModifyNameChance() {
        return modifyNameChance;
    }

    public void setModifyNameChance(Integer modifyNameChance) {
        this.modifyNameChance = modifyNameChance;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }
}

package com.wen.im.common.model.dto.event;

import java.io.Serializable;
import java.util.List;

/**
 * @author wenting
 */
public class OnStatusChangeEvent implements Serializable {
    private List<UserItem> changeList;
    private Integer onlineNum;
    private Integer totalNum;

    public List<UserItem> getChangeList() {
        return changeList;
    }

    public void setChangeList(List<UserItem> changeList) {
        this.changeList = changeList;
    }

    public Integer getOnlineNum() {
        return onlineNum;
    }

    public void setOnlineNum(Integer onlineNum) {
        this.onlineNum = onlineNum;
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        this.totalNum = totalNum;
    }
}

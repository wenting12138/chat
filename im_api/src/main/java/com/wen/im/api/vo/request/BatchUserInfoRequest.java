package com.wen.im.api.vo.request;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @author wenting
 */
public class BatchUserInfoRequest {

    private List<JSONObject> reqList;

    public List<JSONObject> getReqList() {
        return reqList;
    }

    public void setReqList(List<JSONObject> reqList) {
        this.reqList = reqList;
    }
}

package com.wen.im.common.model.dto.msg;

import com.alibaba.fastjson.JSONObject;
import com.wen.im.common.enums.MsgTypeEnum;

import java.util.List;

/**
 * @author wenting
 */
public class MsgBuilder {
    public static JSONObject buildSysMsg(Long roomId, String content, List<Long> atUidList){
//        {"roomId":10009,"msgType":1,"body":{"content":"11","atUidList":[]}};
        JSONObject res = new JSONObject();
        res.put("roomId", roomId);
        res.put("msgType", MsgTypeEnum.SYSTEM.getType());
        JSONObject body = new JSONObject();
        body.put("content", content);
        body.put("atUidList", atUidList);
        res.put("body", body);
        return res;
    }

    public static JSONObject buildTextMsg(Long roomId, String content, List<Long> atUidList){
//        {"roomId":10009,"msgType":1,"body":{"content":"11","atUidList":[]}};
        JSONObject res = new JSONObject();
        res.put("roomId", roomId);
        res.put("msgType", MsgTypeEnum.TEXT.getType());
        JSONObject body = new JSONObject();
        body.put("content", content);
        body.put("atUidList", atUidList);
        res.put("body", body);
        return res;
    }

}

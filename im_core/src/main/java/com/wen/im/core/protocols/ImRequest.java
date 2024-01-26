package com.wen.im.core.protocols;

import com.alibaba.fastjson2.JSON;
import com.wen.im.common.utils.RequestCode;

/**
 * @author wenting
 */
public class ImRequest {
    private int code;
    private ImRequestHeader header;
    private Object body;

    public static ImRequest createRequest(int code, Long uid){
        ImRequest request = new ImRequest();
        request.setCode(code);
        request.setHeader(ImRequestHeader.defaultHeader(uid));
        return request;
    }

    public static ImRequest createRequest(RequestCode requestCode, Long uid){
        ImRequest request = new ImRequest();
        request.setCode(requestCode.getCode());
        request.setHeader(ImRequestHeader.defaultHeader(uid));
        return request;
    }


    public <T> T encodeRequestBody(Class<T> clazz){
        return JSON.parseObject(JSON.toJSONString(this.body), clazz);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ImRequestHeader getHeader() {
        return header;
    }

    public void setHeader(ImRequestHeader header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }
}

package com.wen.im.core.protocols;

import com.wen.im.common.utils.RequestCode;
import com.wen.im.common.utils.ResponseCode;

/**
 * @author wenting
 */
public class ImResponse {
    private int code;
    private Object header;
    private Object body;
    private String remark;

    public static ImResponse result(ResponseCode responseCode, String remark) {
        ImResponse response = new ImResponse();
        response.setRemark(remark);
        response.setCode(responseCode.getCode());
        return response;
    }

    public static ImResponse result(ResponseCode responseCode, Object body, String remark) {
        ImResponse response = new ImResponse();
        response.setRemark(remark);
        response.setCode(responseCode.getCode());
        response.setBody(body);
        return response;
    }

    public static ImResponse result(int requestCode, Object body, String remark) {
        ImResponse response = new ImResponse();
        response.setRemark(remark);
        response.setCode(requestCode);
        response.setBody(body);
        return response;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getHeader() {
        return header;
    }

    public void setHeader(Object header) {
        this.header = header;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

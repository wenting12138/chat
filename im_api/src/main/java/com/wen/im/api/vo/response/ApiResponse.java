package com.wen.im.api.vo.response;

import com.wen.im.common.serirals.Serializer;

import java.io.Serializable;

/**
 * @author wenting
 */
public class ApiResponse implements Serializable {
    private Boolean success;
    private String errMsg;
    private Integer errCode;
    private Object data;

    public ApiResponse() {
    }

    public static ApiResponse success(){
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setData(null);
        return response;
    }


    public static ApiResponse success(Object data){
        ApiResponse response = new ApiResponse();
        response.setSuccess(true);
        response.setData(data);
        return response;
    }

    public static ApiResponse error(String errMsg, Integer errCode){
        ApiResponse response = new ApiResponse();
        response.setSuccess(false);
        response.setErrMsg(errMsg);
        response.setErrCode(errCode);
        return response;
    }


    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public void setErrCode(Integer errCode) {
        this.errCode = errCode;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

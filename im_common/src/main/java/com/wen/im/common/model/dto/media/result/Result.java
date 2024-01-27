package com.wen.im.common.model.dto.media.result;


/**
 * 返回类
 *
 * @author lidaofu
 * @since 2023/4/10
 **/
public class Result<T> {
    /**
     * 状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String msg;

    /**
     * 数据
     */
    private T data;
    public Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

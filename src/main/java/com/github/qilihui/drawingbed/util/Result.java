package com.github.qilihui.drawingbed.util;

import lombok.Data;

/**
 * @author qilihui
 * @date 2021/4/1 15:50
 */
@Data
public class Result {
    //响应业务状态
    private int status;
    //响应消息
    private String msg;
    //响应中的数据
    private Object data;

    public Result() {
    }

    public Result(Object data) {
        this.status = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Result(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 用于有返回结果的成功响应
     *
     * @param data
     * @return
     */
    public static Result ok(Object data) {
        return new Result(200, "", data);
    }

    /**
     * 用于有返回结果的失败响应
     *
     * @param msg 失败提示信息
     * @return
     */
    public static Result fail(String msg) {
        return new Result(400, msg, null);
    }
}

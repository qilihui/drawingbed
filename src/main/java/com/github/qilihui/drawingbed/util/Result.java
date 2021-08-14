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
    /**
     * 200: OK
     * 410: 图片不存在
     * 420: 图片类型错误
     * 430: 达到接口请求速率上限
     * 440: 达到文件大小限制
     * 500: 服务器内部错误
     */
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

    /**
     * 图片不存在
     *
     * @param msg 失败提示信息
     * @return
     */
    public static Result failImgNoExist(String msg) {
        return new Result(410, msg, null);
    }

    /**
     * 图片类型错误
     *
     * @param msg 失败提示信息
     * @return
     */
    public static Result failImgTypeErr(String msg) {
        return new Result(420, msg, null);
    }

    /**
     * 达到接口请求速率上线
     *
     * @param msg 失败提示信息
     * @return
     */
    public static Result failRateLimiter(String msg) {
        return new Result(430, msg, null);
    }

    /**
     * 服务器内部错误
     *
     * @param msg 失败提示信息
     * @return
     */
    public static Result failServerErr(String msg) {
        return new Result(500, msg, null);
    }

    public static Result failFileSizeLimitExceeded() {
        return new Result(440, OjConstant.FILE_SIZE_LIMIT_EXCEEDED, null);
    }
}

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

    public Result(int status, String msg, Object data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static Result result(StatusCode statusCode){
        return new Result(statusCode.getCode(), statusCode.getMsg(), null);
    }

    public static Result result(StatusCode statusCode, Object data){
        return new Result(statusCode.getCode(), statusCode.getMsg(), data);
    }

    public static Result result(StatusCode statusCode,String msg, Object data){
        return new Result(statusCode.getCode(), msg, data);
    }

    public static Result ok(Object data) {
        return result(StatusCode.SUCCESS, data);
    }

    public static Result fail(String msg) {
        return result(StatusCode.UNKNOWN_ERROR, msg, null);
    }

    public static Result failImgNoExist() {
        return result(StatusCode.IMAGE_NOT_FOND);
    }

    public static Result failImgTypeErr() {
        return result(StatusCode.IMAGE_TYPE_ERROR);
    }

    public static Result failRateLimiter() {
        return result(StatusCode.RATE_LIMITER);
    }

    public static Result failServerErr() {
        return result(StatusCode.SERVER_ERROR);
    }

    public static Result failFileSizeLimitExceeded() {
        return result(StatusCode.FILE_SIZE_LIMITER);
    }

    public static Result failTokenInvalid(String msg) {
        return result(StatusCode.TOKEN_INVALID, msg, null);
    }
}

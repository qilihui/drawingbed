package com.github.qilihui.drawingbed.util;

import lombok.Getter;

/**
 * @author qilihui
 * @date 2021/5/16 22:13
 */
@Getter
public enum StatusCode {
    SUCCESS(200, "OK"),
    UNKNOWN_ERROR(400, "未知错误"),
    IMAGE_NOT_FOND(410, "图片不存在"),
    IMAGE_TYPE_ERROR(420, "图片类型错误"),
    RATE_LIMITER(430, "达到接口请求速率上限"),
    FILE_SIZE_LIMITER(440, "达到文件大小限制"),
    TOKEN_INVALID(450, "Token无效"),
    SERVER_ERROR(500, "服务器错误")

    ;

    private Integer code;
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

package com.github.qilihui.drawingbed.util;

/**
 * @author qilihui
 * @date 2021/5/16 22:13
 */
public enum StatusCode {
    SUCCESS(200, "操作成功"),
    SERVER_ERROR(500, "服务器错误")

    ;

    private Integer code;
    private String msg;

    StatusCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

package com.github.qilihui.drawingbed.exception;

/**
 * @author qilihui
 * @date 2021/5/16 13:21
 */
public class RateLimiterException extends RuntimeException{
    public RateLimiterException() {
        super();
    }

    public RateLimiterException(String message) {
        super(message);
    }
}

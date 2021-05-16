package com.github.qilihui.drawingbed.config;

import com.github.qilihui.drawingbed.exception.RateLimiterException;
import com.github.qilihui.drawingbed.util.Result;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author qilihui
 * @date 2021/4/20 9:36
 */
@RestControllerAdvice
public class DrawingBedExceptionHandler {

    @ExceptionHandler(value = RateLimiterException.class)
    public Result rateLimiterExceptionHandler(RateLimiterException e) {
        return Result.failRateLimiter(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception e) {
        return Result.fail(e.getMessage());
    }
}

package com.github.qilihui.drawingbed.config.aop;

import cn.hutool.core.util.StrUtil;
import com.github.qilihui.drawingbed.annotation.RateLimiter;
import com.github.qilihui.drawingbed.exception.SecurityException;
import com.github.qilihui.drawingbed.util.*;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author qilihui
 * @date 2021/5/16 12:38
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RateLimitAspect {
    private final static String SEPARATOR = ":";
    private final static String REDIS_LIMIT_KEY_PREFIX = "limit:";
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisScript<Long> limitRedisScript;

    /**
     * 上传限流接口切点
     */
    @Pointcut("@annotation(com.github.qilihui.drawingbed.annotation.RateLimiter)")
    public void rateLimit() {
    }

    @Around("rateLimit()")
    public Object aroundUpload(ProceedingJoinPoint point) throws Throwable {
        //Token验证
        HttpServletRequest request = ProjectHttpUtil.getRequest();
        String jwt = JwtUtil.getJwtFromRequest(request);
        try {
            if (StrUtil.isNotEmpty(jwt)) {
                String s = JwtUtil.parseJWT(jwt);
                if (s != null) {
                    return point.proceed();
                }
            }
        } catch (SecurityException e) {
            log.info(e.getMessage());
            ProjectHttpUtil.setResponse(Result.failTokenInvalid(e.getMessage()));
            return null;
        }
        //请求限流
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        // 通过 AnnotationUtils.findAnnotation 获取 RateLimiter 注解
        RateLimiter rateLimiter = AnnotationUtils.findAnnotation(method, RateLimiter.class);
        if (rateLimiter != null) {
            String key = rateLimiter.key();
            // 默认用类名+方法名做限流的 key 前缀
            if (StrUtil.isBlank(key)) {
                key = method.getDeclaringClass().getName() + StrUtil.DOT + method.getName();
            }
            // 最终限流的 key 为 前缀 + IP地址
            String header = request.getHeader("User-Agent");
            UserAgent userAgent = UserAgent.parseUserAgentString(header);
            key = key
                    + SEPARATOR + header
                    + SEPARATOR + userAgent.getBrowser().toString()
                    + SEPARATOR + userAgent.getOperatingSystem().toString()
                    + SEPARATOR + ThreadLocalUtil.getIp();

            long max = rateLimiter.max();
            long timeout = rateLimiter.timeout();
            TimeUnit timeUnit = rateLimiter.timeUnit();
            boolean limited = shouldLimited(key, max, timeout, timeUnit);
            if (limited) {
                ProjectHttpUtil.setResponse(Result.failRateLimiter());
                return null;
            }
        }

        return point.proceed();
    }

    private boolean shouldLimited(String key, long max, long timeout, TimeUnit timeUnit) {
        // 最终的 key 格式为：
        // limit:自定义key:UserAgent:Browser:OS:IP
        // limit:类名.方法名:UserAgent:Browser:OS:IP
        key = REDIS_LIMIT_KEY_PREFIX + key;
        // 统一使用单位毫秒
        long ttl = timeUnit.toMillis(timeout);
        // 当前时间毫秒数
        long now = Instant.now().toEpochMilli();
        long expired = now - ttl;
        // 注意这里必须转为 String,否则会报错 java.lang.Long cannot be cast to java.lang.String
        Long executeTimes = stringRedisTemplate.execute(limitRedisScript, Collections.singletonList(key), now + "", ttl + "", expired + "", max + "");
        if (executeTimes != null) {
            if (executeTimes == 0) {
                log.warn("【{}】在单位时间 {} 毫秒内已达到访问上限，当前接口上限 {}", key, ttl, max);
                return true;
            } else {
                log.info("【{}】在单位时间 {} 毫秒内访问 {} 次", key, ttl, executeTimes);
                return false;
            }
        }
        return false;
    }
}

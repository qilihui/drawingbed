package com.github.qilihui.drawingbed.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.github.qilihui.drawingbed.config.DrawingBedConfig;
import com.github.qilihui.drawingbed.exception.SecurityException;
import io.jsonwebtoken.*;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author qilihui
 * @date 2021/5/16 21:48
 */
@Import(cn.hutool.extra.spring.SpringUtil.class)
public class JwtUtil {
    private final static DrawingBedConfig config = SpringUtil.getBean("drawingBedConfig");
    private final static StringRedisTemplate stringRedisTemplate = SpringUtil.getBean("stringRedisTemplate");
    private final static String REDIS_JWT_KEY_PREFIX = "jwt:";

    public static String createJWT() {
        Date now = new Date();
        String uuid = IdUtil.randomUUID();
        JwtBuilder builder = Jwts.builder().setId(uuid).setIssuedAt(now).signWith(SignatureAlgorithm.HS256, config.getJwt().getKey());

        // 设置过期时间
        Long ttl = config.getJwt().getTtl();
        if (ttl > 0) {
            builder.setExpiration(DateUtil.offsetMillisecond(now, ttl.intValue()));
        }

        String jwt = builder.compact();
        // 将生成的JWT保存至Redis
        stringRedisTemplate.opsForValue().set(REDIS_JWT_KEY_PREFIX + uuid, jwt, ttl, TimeUnit.MILLISECONDS);
        return jwt;
    }

    public static String parseJWT(String jwt) throws SecurityException {
        try {
            Claims claims = Jwts.parser().setSigningKey(config.getJwt().getKey()).parseClaimsJws(jwt).getBody();
            String id = claims.getId();
            String redisKey = REDIS_JWT_KEY_PREFIX + id;

            // 校验redis中的JWT是否存在
            Long expire = stringRedisTemplate.getExpire(redisKey, TimeUnit.MILLISECONDS);
            if (Objects.isNull(expire) || expire <= 0) {
                throw new SecurityException("Token 可用，但Redis中被移除");
            }

            // 校验redis中的JWT是否与当前的一致，不一致则代表用户已注销/用户在不同设备登录，均代表JWT已过期
            String redisToken = stringRedisTemplate.opsForValue().get(redisKey);
            if (!StrUtil.equals(jwt, redisToken)) {
                throw new SecurityException("Token 与redis中不一致");
            }
            return id;
        } catch (ExpiredJwtException e) {
            throw new SecurityException("Token 已过期");
        } catch (UnsupportedJwtException e) {
            throw new SecurityException("不支持的 Token");
        } catch (MalformedJwtException e) {
            throw new SecurityException("Token 无效");
        } catch (SignatureException e) {
            throw new SecurityException("无效的 Token 签名");
        } catch (IllegalArgumentException e) {
            throw new SecurityException("Token 参数不存在");
        }
    }

    /**
     * 从 request 的 header 中获取 JWT
     *
     * @param request 请求
     * @return JWT
     */
    public static String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StrUtil.isNotBlank(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

package com.github.qilihui.drawingbed.util;

import com.github.qilihui.drawingbed.exception.SecurityException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * @author qilihui
 * @date 2021/5/16 22:25
 */
@SpringBootTest
class JwtUtilTest {
    private String jwt = null;

    @Test
    void createJWT() {
        jwt = JwtUtil.createJWT();
        System.out.println(jwt);
    }

    @Test
    void parseJWT() {
        try {
            System.out.println(JwtUtil.parseJWT("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI0NjE2NjQwNS0yYTE1LTQ0MjUtOTc5NS1mYjcyMzNkNTkwM2QiLCJpYXQiOjE2MjExNzUzOTEsImV4cCI6MTYyMTE3NTQ1MX0.TPOiAtOkBr03vG6lfhJwrPlcyVS2Yt7kvzzHfLFLyp4"));
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
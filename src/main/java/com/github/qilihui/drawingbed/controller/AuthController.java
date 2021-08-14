package com.github.qilihui.drawingbed.controller;

import com.github.qilihui.drawingbed.util.JwtUtil;
import com.github.qilihui.drawingbed.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qilihui
 * @date 2021/6/11 11:52
 */
@RestController
public class AuthController {

    @RequestMapping("/auth")
    public Result getToken(@RequestParam("username") String username, @RequestParam("password") String password) {
        return Result.ok(JwtUtil.createJWT());
    }
}

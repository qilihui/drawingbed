package com.github.qilihui.drawingbed;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qilihui
 * @date 2021/8/14 18:31
 */
@RestController
public class CheckController {
    @RequestMapping("/ping")
    public String ping(){
        return "OK";
    }
}

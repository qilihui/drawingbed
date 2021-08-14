package com.github.qilihui.drawingbed;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qilihui
 * @date 2021/8/14 18:31
 */
@RestController
@Slf4j
public class CheckController {
    @RequestMapping("/ping")
    public String ping(){
        log.info("执行心跳检测");
        return "OK-";
    }
}

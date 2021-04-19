package com.github.qilihui.drawingbed;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

//@SpringBootTest
class DrawingbedApplicationTests {

//    @Test
    void contextLoads() {
        HashMap<String, Object> paramMap = new HashMap<>();
//文件上传只需将参数中的键指定（默认file），值设为文件对象即可，对于使用者来说，文件上传与普通表单提交并无区别
        paramMap.put("file", FileUtil.file("C:\\Users\\Tom\\Pictures\\Spotlight\\Vertical\\1ac56ece94e2f8b9483ef456092be11618351f242213f6a2c740dfc33a2551bc.jpg"));

        String result = HttpUtil.post("http://localhost:8080/upload", paramMap);
        System.out.println(result);
    }

}

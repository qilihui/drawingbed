package com.github.qilihui.drawingbed;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;

//@SpringBootTest
class DrawingbedApplicationTests {

    @Test
    void contextLoads() {
        HashMap<String, Object> paramMap = new HashMap<>();
//文件上传只需将参数中的键指定（默认file），值设为文件对象即可，对于使用者来说，文件上传与普通表单提交并无区别
        paramMap.put("file", FileUtil.file("C:\\Users\\Tom\\Desktop\\src=http___09imgmini.eastday.com_mobile_20180916_20180916203923_d41d8cd98f00b204e9800998ecf8427e_2.jpeg&refer=http___09imgmini.eastday.jfif"));

        String result = HttpUtil.post("http://abiao.me:6688/upload", paramMap);
        System.out.println(result);
    }
    private String getYearMonthDayUrl() {
        Date date = DateUtil.date();
        return DateUtil.format(date, "/yyyy/MM/dd/");
    }
    @Test
    void testDate(){
        System.out.println(getYearMonthDayUrl());
    }

}

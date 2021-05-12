package com.github.qilihui.drawingbed;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import com.github.qilihui.drawingbed.util.NameUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
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
    void testDate() {
        System.out.println(getYearMonthDayUrl());
    }

    @Test
    void testToPre2Bit() {
        long a = (long) Math.pow(2, 8) - 1;
        System.out.println(Integer.toBinaryString((int) a));
        System.out.println(Integer.toBinaryString((int) insertRandomBitPer5Bits(a)));
        System.out.println(Integer.toBinaryString((int) insertRandomBitPer5Bits(a)));
        System.out.println(Integer.toBinaryString((int) insertRandomBitPer5Bits(a)));
        System.out.println(Integer.toBinaryString((int) insertRandomBitPer5Bits(a)));
        System.out.println(Integer.toBinaryString((int) insertRandomBitPer5Bits(a)));
    }

    private long insertRandomBitPer5Bits(long val) {
        long result = val;
        long high = val;
        for (int i = 0; i < 10; i++) {
            if (high == 0) {
                break;
            }
            int pos = 2 + 2 * i + i;
            high = result >> pos;
            result = ((high << 1 | RandomUtil.randomInt(0, 2)) << pos)
                    | (result & (-1L >>> (64 - pos)));
        }
        return result;
    }

    @Test
    void md5TimeTest() {
        File file = FileUtil.file("C:\\Users\\Tom\\Pictures\\LenovoWallPaper.jpg");
        File file2 = FileUtil.file("D:\\Tools\\other\\Music\\董小姐-宋冬野（Cover by 陈一发儿）.mp4");
        long l = System.currentTimeMillis();
        System.out.println(SecureUtil.md5(file));
//        NameUtil.getFileNameByThisDayTime("jpg");
        System.out.println(System.currentTimeMillis() - l);
//        long f = System.currentTimeMillis();
//        System.out.println(SecureUtil.md5(file));
//        System.out.println(System.currentTimeMillis() - f);
    }

    @Test
    void createTempFileTest() throws IOException {
        File file = new File("D:\temp\test");
        File tempFile = File.createTempFile(IdUtil.randomUUID(), ".jpg", file);
    }
}

package com.github.qilihui.drawingbed.util;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author qilihui
 * @date 2021/4/18 22:17
 */
public class NameUtil {
    private static final String BASE = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Set<String> FILE_TYPE_SET = new HashSet<>();

    static {
        FILE_TYPE_SET.add("jpg");
        FILE_TYPE_SET.add("jpeg");
        FILE_TYPE_SET.add("png");
        FILE_TYPE_SET.add("gif");
    }

    /**
     * @param num 正整数
     * @return 62进制数
     */
    public static String toBase62(long num) {
        StringBuilder sb = new StringBuilder();
        int targetBase = BASE.length();
        do {
            int i = (int) (num % targetBase);
            sb.append(BASE.charAt(i));
            num /= targetBase;
        } while (num > 0);

        return sb.reverse().toString();
    }

    /**
     * @param input 62进制数
     * @return 正整数
     */
    public static long toBase10(String input) {
        int srcBase = BASE.length();
        long id = 0;
        String r = new StringBuilder(input).reverse().toString();

        for (int i = 0; i < r.length(); i++) {
            int charIndex = BASE.indexOf(r.charAt(i));
            id += charIndex * (long) Math.pow(srcBase, i);
        }

        return id;
    }


    /**
     * @param val 正整数
     * @return 加入随机数
     */
    public static long insertRandomBitPer3Bits(long val) {
        long result = val;
        long high = val;
        for (int i = 0; i < 10; i++) {
            if (high == 0) {
                break;
            }
            int pos = 3 + 3 * i + i;
            high = result >> pos;
            result = ((high << 1 | RandomUtil.randomInt(0, 2)) << pos)
                    | (result & (-1L >>> (64 - pos)));
        }
        return result;
    }

    /**
     * id转shortUrl
     *
     * @param id id
     * @return url
     */
    public static String numToShortUrl(long id) {
        long l = insertRandomBitPer3Bits(id);
        return toBase62(l);
    }

    /**
     * 从当天开始 根据时间戳获取文件名
     * @param type 文件格式
     * @return 文件名
     */
    public static String getFileNameByThisDayTime(String type) {
        DateTime dateTime = DateUtil.beginOfDay(new Date());
        long fileName = DateUtil.current() - dateTime.getTime() + 10000000L;
        return numToShortUrl(fileName) + "." + type;
    }

    public static boolean isImage(String name) {
        return FILE_TYPE_SET.contains(name.toLowerCase());
    }
}

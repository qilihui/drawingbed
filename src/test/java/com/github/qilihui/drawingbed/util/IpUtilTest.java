package com.github.qilihui.drawingbed.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author qilihui
 * @date 2021/5/16 19:47
 */
class IpUtilTest {

    @Test
    void ipV4ToLong() {
        System.out.println(IpUtil.isIPv4Private("127.0.0.1"));
        System.out.println(IpUtil.ipV4ToLong("127.0.0.1"));
    }
}
package com.github.qilihui.drawingbed.util;

import cn.hutool.json.JSONUtil;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * @author qilihui
 * @date 2021/8/15 16:21
 */
public class ProjectHttpUtil {
    public static HttpServletRequest getRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Objects.requireNonNull(attributes).getRequest();
    }

    public static void setResponse(Object object){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        ServletOutputStream outputStream = null;
        try {
            assert attributes != null;
            assert attributes.getResponse() != null;
            outputStream = attributes.getResponse().getOutputStream();
            attributes.getResponse().setHeader("content-type", "application/json;charset=UTF-8");
            assert outputStream != null;
            outputStream.write(JSONUtil.toJsonStr(object).getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

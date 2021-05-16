package com.github.qilihui.drawingbed.interceptor;

import com.github.qilihui.drawingbed.util.IpUtil;
import com.github.qilihui.drawingbed.util.ThreadLocalUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qilihui
 * @date 2021/5/16 20:10
 */
public class ThreadLocalInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String ipAddr = IpUtil.getIpAddr(request);
        ThreadLocalUtil.addIp(ipAddr);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        ThreadLocalUtil.remove();
    }
}

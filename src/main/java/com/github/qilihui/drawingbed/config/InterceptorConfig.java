package com.github.qilihui.drawingbed.config;

import com.github.qilihui.drawingbed.interceptor.ThreadLocalInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author qilihui
 * @date 2021/5/16 20:14
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Bean
    ThreadLocalInterceptor threadLocalInterceptor() {
        return new ThreadLocalInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(threadLocalInterceptor());
    }
}

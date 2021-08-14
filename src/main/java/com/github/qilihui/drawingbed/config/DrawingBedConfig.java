package com.github.qilihui.drawingbed.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * @author qilihui
 * @date 2021/5/12 22:16
 */
@Component
@ConfigurationProperties(prefix = "drawing")
@Data
public class DrawingBedConfig {
    private String path;
    private Integer retryCount;

    @NestedConfigurationProperty
    private Jwt jwt;

    @Data
    public static class Jwt {
        private String key;
        private Long ttl;
    }
}

package com.abba.boot.autoconfigure.fastdfs;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.springframework.util.StringUtils.hasText;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
@Configuration
@ConfigurationProperties(prefix = "fastdfs")
@Data
public class FastDfsProperties {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private String trackerServers;
    private Integer connectTimeoutInSeconds;
    private Integer networkTimeoutInSeconds;
    private Charset charset = DEFAULT_CHARSET;
    private Integer httpTrackerHttpPort = 80;
    private Boolean httpSecretKey = false;
    private String httpAntiStealToken;
    private ConnectionPool connectionPool;

    @Data
    public static class ConnectionPool {
        private Boolean enabled = true;
        private Integer maxCountPerEntry = 500;
        private Integer maxIdleTime = 3600;
        private Integer maxWaitTimeInMs = 1000;
    }
}

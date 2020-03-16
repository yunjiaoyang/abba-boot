package com.abba.boot.autoconfigure.alibaba;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.springframework.util.StringUtils.hasText;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */

@Configuration
@ConfigurationProperties(prefix = "alibaba.oss")
@Data
@Slf4j
public class AlibabaOssProperties {
    /**
     * 存储地域
     */
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String domain;

    public boolean validate() {
        boolean success = true;

        if (!hasText(this.endpoint)) {
            success = false;
            log.warn("configuration parameter 'endpoint' must have value");
        }

        if (!hasText(this.accessKeyId)) {
            success = false;
            log.warn("configuration parameter 'access_key_id' must have value");
        }

        if (!hasText(this.accessKeySecret)) {
            success = false;
            log.warn("configuration parameter 'access-key-secret' must have value");
        }

        if (!hasText(this.bucketName)) {
            success = false;
            log.warn("configuration parameter 'bucket-name' must have value, or use default value");
        }

        return success;
    }
}

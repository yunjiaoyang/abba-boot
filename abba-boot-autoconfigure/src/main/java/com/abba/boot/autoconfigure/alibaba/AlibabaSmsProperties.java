package com.abba.boot.autoconfigure.alibaba;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.springframework.util.StringUtils.hasText;

/**
 * 配置文件
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
@Data
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "alibaba.sms")
public class AlibabaSmsProperties {

    private String accessKeyId;

    private String secret;

    private String signName;

    private String regionId = "cn-hangzhou";

    private String domain = "dysmsapi.aliyuncs.com";

    private String version = "2017-05-25";

    /**
     *
     * @return
     */
    public boolean validate() {
        boolean success = true;

        if (!hasText(this.accessKeyId)) {
            success = false;
            log.warn("configuration parameter 'access_key_id' must have value");
        }

        if (!hasText(this.secret)) {
            success = false;
            log.warn("configuration parameter 'secret' must have value");
        }

        if (!hasText(this.signName)) {
            success = false;
            log.warn("configuration parameter 'sign-name' must have value");
        }

        if (!hasText(this.regionId)) {
            success = false;
            log.warn("configuration parameter 'region-id' must have value, or use default value");
        }

        if (!hasText(this.domain)) {
            success = false;
            log.warn("configuration parameter 'domain' must have value, or use default value");
        }

        if (!hasText(this.version)) {
            success = false;
            log.warn("configuration parameter 'version' must have value, or use default value");
        }

        return success;
    }
}

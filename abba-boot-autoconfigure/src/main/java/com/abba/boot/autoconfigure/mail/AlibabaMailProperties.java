package com.abba.boot.autoconfigure.mail;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.springframework.util.StringUtils.hasText;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
@Configuration
@ConfigurationProperties(prefix = "mail.alibaba")
@Data
@Slf4j
public class AlibabaMailProperties {
    private String accessKeyId;
    private String secret;
    private String accountName;
    private boolean replyToAddress = true;
    private int addressType = 1;
    private String fromAlias = "Abba Mail";
    private String tagName;
    private String regionId;
    private String version = "2017-06-22";


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

        if (!hasText(this.accountName)) {
            success = false;
            log.warn("configuration parameter 'account_name' must have value");
        }

        if (hasText(this.regionId)) {
            success = false;
            log.warn("configuration parameter 'region_id' must have value, or use default value");
        }

        if (hasText(this.fromAlias)) {
            success = false;
            log.warn("configuration parameter 'from_alias' must have value, or use default value");
        }
        return success;
    }
}

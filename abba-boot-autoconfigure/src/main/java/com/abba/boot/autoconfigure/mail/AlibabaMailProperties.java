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
@ConfigurationProperties(prefix = "alibaba.alibaba")
@Data
public class AlibabaMailProperties {
    private String accessKeyId;
    private String secret;
    private String accountName;
    private Boolean replyToAddress = true;
    private Integer addressType = 1;
    private String fromAlias = "Abba Mail";
    private String tagName;
    private String regionId;
    private String version = "2017-06-22";
}

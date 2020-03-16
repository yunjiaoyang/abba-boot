package com.abba.boot.autoconfigure.sms;

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
}

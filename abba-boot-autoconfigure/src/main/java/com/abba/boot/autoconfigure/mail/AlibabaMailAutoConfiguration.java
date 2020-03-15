package com.abba.boot.autoconfigure.mail;

import com.aliyuncs.IAcsClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
@Configuration
@EnableConfigurationProperties(AlibabaMailProperties.class)
@ConditionalOnClass(IAcsClient.class)
@ConditionalOnProperty(prefix = "mail.alibaba")
public class AlibabaMailAutoConfiguration {
    private final AlibabaMailProperties properties;

    public AlibabaMailAutoConfiguration(AlibabaMailProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public AlibabaMailService alibabaMailService() {
        return new AlibabaMailService(this.properties);
    }
}

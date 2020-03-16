package com.abba.boot.autoconfigure.alibaba;

import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
 * @create 2020/3/13
 */
@Configuration
@ConditionalOnClass(SendSmsRequest.class)
@ConditionalOnProperty(prefix = "sms.alibaba")
@EnableConfigurationProperties(AlibabaSmsProperties.class)
public class AlibabaSmsAutoConfiguration {
    private final AlibabaSmsProperties properties;
    private final ObjectMapper objectMapper;


    public AlibabaSmsAutoConfiguration(AlibabaSmsProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;
    }

    @Bean
    @ConditionalOnMissingBean
    public AlibabaSmsService alibabaService() {
        return new AlibabaSmsService(this.properties, this.objectMapper);
    }
}

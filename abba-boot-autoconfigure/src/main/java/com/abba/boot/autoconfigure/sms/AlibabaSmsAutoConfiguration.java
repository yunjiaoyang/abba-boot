package com.abba.boot.autoconfigure.sms;

import com.abba.boot.plugin.alibaba.sms.AlibabaSmsService;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
@ConditionalOnClass(IAcsClient.class)
@ConditionalOnProperty(prefix = "alibaba.sms")
@EnableConfigurationProperties(AlibabaSmsProperties.class)
@Slf4j
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
        log.info("Alibaba SMS initializing...");

        DefaultProfile profile = DefaultProfile.getProfile(this.properties.getRegionId(),
                this.properties.getAccessKeyId(),
                this.properties.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);
        return new AlibabaSmsService(properties.getDomain(), properties.getVersion(), properties.getSignName(), client, objectMapper);
    }
}

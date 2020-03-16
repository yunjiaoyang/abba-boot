package com.abba.boot.autoconfigure.mail;

import com.abba.boot.plugin.alibaba.mail.AlibabaMailService;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
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
 * @create 2020/3/14
 */
@Configuration
@EnableConfigurationProperties(AlibabaMailProperties.class)
@ConditionalOnClass(IAcsClient.class)
@ConditionalOnProperty(prefix = "alibaba.mail")
@Slf4j
public class AlibabaMailAutoConfiguration {
    private final AlibabaMailProperties properties;

    public AlibabaMailAutoConfiguration(AlibabaMailProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public AlibabaMailService alibabaMailService() {
        log.info("Alibaba Mail initializing...");


        DefaultProfile profile = DefaultProfile.getProfile(this.properties.getRegionId(),
                this.properties.getAccessKeyId(),
                this.properties.getSecret());
        IAcsClient client = new DefaultAcsClient(profile);

        return new AlibabaMailService(properties.getVersion(),
                properties.getAccountName(),
                properties.getAddressType(),
                properties.getReplyToAddress(),
                properties.getFromAlias(),
                properties.getTagName(),
                client);
    }
}

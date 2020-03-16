package com.abba.boot.autoconfigure.oss;

import com.abba.boot.plugin.alibaba.oss.AlibabaOssPrcoess;
import com.abba.boot.plugin.alibaba.oss.AlibabaOssService;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.springframework.util.StringUtils.hasText;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */
@Configuration
@EnableConfigurationProperties(AlibabaOssProperties.class)
@ConditionalOnClass(OSS.class)
@ConditionalOnProperty(prefix = "alibaba.oss")
@Slf4j
public class AlibabaOssAutoConfiguration {
    private final AlibabaOssProperties properties;
    private final ClientBuilderConfiguration configuration;
    private final AlibabaOssPrcoess process;

    public AlibabaOssAutoConfiguration(AlibabaOssProperties properties1,
                                       ClientBuilderConfiguration configuration,
                                       ObjectProvider<AlibabaOssPrcoess> provider) {
        this.properties = properties1;
        this.configuration = configuration;
        this.process = provider.getIfAvailable();
    }

    @Bean
    @ConditionalOnMissingBean
    public AlibabaOssService alibabaOssService() {
        log.info("Alibaba OSS initializing...");

        if (log.isDebugEnabled()) {
            log.debug(this.properties.toString());
        }

        OSS oss = null;
        if (hasText(properties.getSecurityToken())) {
            // 使用STS
            oss = new OSSClientBuilder().build(properties.getEndpoint(),
                    properties.getAccessKeyId(),
                    properties.getAccessKeySecret(),
                    properties.getSecurityToken());
        } else if (configuration != null) {
            oss = new OSSClientBuilder().build(properties.getEndpoint(),
                    properties.getAccessKeyId(),
                    properties.getAccessKeySecret(),
                    configuration);
        } else {
            oss = new OSSClientBuilder().build(properties.getEndpoint(),
                    properties.getAccessKeyId(),
                    properties.getAccessKeySecret());
        }

        return new AlibabaOssService(properties.getBucketName(), oss, this.process);
    }

}

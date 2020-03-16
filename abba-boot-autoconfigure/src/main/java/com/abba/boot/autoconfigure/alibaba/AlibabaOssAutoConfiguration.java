package com.abba.boot.autoconfigure.alibaba;

import com.aliyun.oss.OSS;
import org.springframework.beans.factory.ObjectProvider;
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
@EnableConfigurationProperties(AlibabaOssProperties.class)
@ConditionalOnClass(OSS.class)
@ConditionalOnProperty(prefix = "oss.alibaba")
public class AlibabaOssAutoConfiguration {
    private final AlibabaOssProperties properties;
    private final AlibabaOssPrcoess process;

    public AlibabaOssAutoConfiguration(AlibabaOssProperties properties,
                                       ObjectProvider<AlibabaOssPrcoess> provider) {
        this.properties = properties;
        this.process = provider.getIfAvailable();
    }

    @Bean
    @ConditionalOnMissingBean
    public AlibabaOssService alibabaOssService() {
        AlibabaOssService apiBootOssService = new AlibabaOssService(this.properties, this.process);
        return apiBootOssService;
    }
}

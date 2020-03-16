package com.abba.boot.autoconfigure.oss;

import com.abba.boot.plugin.alibaba.oss.AlibabaOssService;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.extern.slf4j.Slf4j;
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
@ConditionalOnProperty(prefix = "alibaba.oss.client")
@Slf4j
public class AlibabaClientAutoConfiguration {
    private final AlibabaOssClientProperties properties;

    public AlibabaClientAutoConfiguration(AlibabaOssClientProperties properties) {
        this.properties = properties;
    }


    @Bean
    @ConditionalOnMissingBean
    public ClientBuilderConfiguration clientBuilderConfiguration() {
        if (log.isDebugEnabled()) {
            log.debug(this.properties.toString());
        }
        ClientBuilderConfiguration conf = new ClientBuilderConfiguration();
        if(properties.getMaxConnections() != null) {
            conf.setMaxConnections(this.properties.getMaxConnections());
        }

        if (properties.getSocketTimeout() != null) {
            conf.setSocketTimeout(properties.getSocketTimeout());
        }

        if (properties.getConnectionTimeout() != null) {
            conf.setConnectionTimeout(properties.getConnectionTimeout());
        }

        if (properties.getConnectionRequestTimeout() != null) {
            conf.setConnectionRequestTimeout(properties.getConnectionRequestTimeout());
        }

        if (properties.getIdleConnectionTime() != null) {
            conf.setIdleConnectionTime(properties.getIdleConnectionTime());
        }

        if (properties.getMaxErrorRetry() != null) {
            conf.setMaxErrorRetry(properties.getMaxErrorRetry());
        }

        if (properties.getSupportCname() != null) {
            conf.setSupportCname(properties.getSupportCname());
        }

        if (properties.getSldEnabled() != null) {
            conf.setSLDEnabled(properties.getSldEnabled());
        }

        if (properties.getProtocol() != null) {
            conf.setProtocol(properties.getProtocol());
        }

        if (hasText(properties.getUserAgent())) {
            conf.setUserAgent(properties.getUserAgent());
        }

        if (hasText(properties.getProxyHost())) {
            conf.setProxyHost(properties.getProxyHost());
        }

        if (properties.getProxyPort() != null) {
            conf.setProxyPort(properties.getProxyPort());
        }

        if (hasText(properties.getProxyUsername())) {
            conf.setProxyUsername(properties.getProxyUsername());
        }

        if (hasText(properties.getProxyPassword())) {
            conf.setProxyPassword(properties.getProxyPassword());
        }

        return conf;
    }
}

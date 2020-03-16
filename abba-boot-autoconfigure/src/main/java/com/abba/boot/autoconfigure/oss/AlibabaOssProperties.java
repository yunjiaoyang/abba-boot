package com.abba.boot.autoconfigure.oss;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/13
 */

@Configuration
@ConfigurationProperties(prefix = "alibaba.oss")
@Getter
@Setter
public class AlibabaOssProperties {
    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String securityToken;

    @Override
    public String toString() {
        return "AlibabaOssProperties{" +
                "endpoint='" + endpoint + '\'' +
                ", accessKeyId='" + accessKeyId + '\'' +
                ", accessKeySecret='******"  + '\'' +
                ", bucketName='" + bucketName + '\'' +
                ", securityToken='******" + '\'' +
                '}';
    }
}

package com.abba.boot.autoconfigure.oss;

import com.aliyun.oss.common.comm.Protocol;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/16
 */
@Configuration
@ConfigurationProperties(prefix = "alibaba.oss.client")
@Data
public class AlibabaOssClientProperties {
    /**
     * 允许打开的最大HTTP连接数。默认为1024
     */
    private Integer maxConnections;

    /**
     * Socket层传输数据的超时时间（单位：毫秒）。默认为50000毫秒
     */
    private Integer socketTimeout;

    /**
     * 建立连接的超时时间（单位：毫秒）。默认为50000毫秒
     */
    private Integer connectionTimeout;

    /**
     * 从连接池中获取连接的超时时间（单位：毫秒）。默认不超时
     */
    private Integer connectionRequestTimeout;

    /**
     * 连接空闲超时时间，超时则关闭连接（单位：毫秒）。默认为60000毫秒
     */
    private Integer idleConnectionTime;

    /**
     * 请求失败后最大的重试次数。默认3次
     */
    private Integer maxErrorRetry;

    /**
     * 是否支持CNAME作为Endpoint，默认支持CNAME
     */
    private Boolean supportCname;

    /**
     * 是否开启二级域名（Second Level Domain）的访问方式，默认不开启
     */
    private Boolean sldEnabled;

    /**
     * 连接OSS所采用的协议（HTTP/HTTPS），默认为HTTP
     */
    private Protocol protocol;

    /**
     * 用户代理，指HTTP的User-Agent头。默认为”aliyun-sdk-java”
     */
    private String userAgent;

    /**
     * 代理服务器主机地址
     */
    private String proxyHost;

    /**
     * 代理服务器端口
     */
    private Integer proxyPort;

    /**
     * 代理服务器验证的用户名
     */
    private String proxyUsername;

    /**
     * 代理服务器验证的密码
     */
    private String proxyPassword;
}

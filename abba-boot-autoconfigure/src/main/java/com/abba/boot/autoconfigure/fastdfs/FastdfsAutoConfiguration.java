package com.abba.boot.autoconfigure.fastdfs;

import com.abba.boot.plugin.fastdfs.FastDfsException;
import com.abba.boot.plugin.fastdfs.FastDfsService;
import lombok.extern.slf4j.Slf4j;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;

/**
 * TODO
 *
 * @author Lucky.Yang
 * @create 2020/3/14
 */
@Configuration
@EnableConfigurationProperties(FastDfsProperties.class)
@ConditionalOnClass(ClientGlobal.class)
@ConditionalOnProperty(prefix = "fastdfs")
@Slf4j
public class FastdfsAutoConfiguration {
    private final FastDfsProperties properties;

    public FastdfsAutoConfiguration(FastDfsProperties properties) {
        this.properties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    public FastDfsService alibabaMailService() {
        log.info("FastDfs Service initializing...");
        if (log.isDebugEnabled()) {
            log.debug(properties.toString());
        }

        Properties prop = new Properties();
        prop.put(ClientGlobal.PROP_KEY_CONNECT_TIMEOUT_IN_SECONDS, properties.getConnectTimeoutInSeconds());
        prop.put(ClientGlobal.PROP_KEY_NETWORK_TIMEOUT_IN_SECONDS, properties.getNetworkTimeoutInSeconds());
        prop.put(ClientGlobal.PROP_KEY_CHARSET, properties.getCharset());
        prop.put(ClientGlobal.PROP_KEY_HTTP_ANTI_STEAL_TOKEN, properties.getHttpAntiStealToken());
        prop.put(ClientGlobal.PROP_KEY_HTTP_SECRET_KEY, properties.getHttpSecretKey());
        prop.put(ClientGlobal.PROP_KEY_HTTP_TRACKER_HTTP_PORT, properties.getHttpTrackerHttpPort());
        prop.put(ClientGlobal.PROP_KEY_TRACKER_SERVERS, properties.getTrackerServers());

        // POOL
        prop.put("fastdfs.connection_pool.enabled", properties.getConnectionPool().getEnabled());
        prop.put("fastdfs.connection_pool.max_count_per_entry", properties.getConnectionPool().getMaxCountPerEntry());
        prop.put("fastdfs.connection_pool.max_idle_time", properties.getConnectionPool().getMaxIdleTime());
        prop.put("fastdfs.connection_pool.max_wait_time_in_ms", properties.getConnectionPool().getMaxWaitTimeInMs());
        try {
            ClientGlobal.initByProperties(prop);

            final TrackerClient trackerC = new TrackerClient();
            final TrackerServer trackerS = trackerC.getTrackerServer();
            final StorageServer storageS = trackerC.getStoreStorage(trackerS);
            final StorageClient1 storageC = new StorageClient1(trackerS, storageS);

            return new FastDfsService(trackerS, trackerC, storageS, storageC);
        } catch (IOException | MyException e) {
            throw new FastDfsException("FastDfs Service initialization error", e);
        }


    }
}

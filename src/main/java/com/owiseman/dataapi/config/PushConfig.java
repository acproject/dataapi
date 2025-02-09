package com.owiseman.dataapi.config;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class PushConfig {
    @Bean
    @ConditionalOnMissingBean
    public EventLoopGroup apnsEventLoopGroup(ApnsConfig config) {
        return new NioEventLoopGroup(config.getToThreads());
    }

    @Bean(name ="pushTaskExecutor")
    public Executor pushTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("apns-push-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean(destroyMethod = "close")
    public ApnsClient apnsClient(ApnsConfig config, EventLoopGroup eventLoopGroup)
        throws IOException, NoSuchAlgorithmException, InvalidKeyException {
        return new ApnsClientBuilder()
                .setApnsServer(config.isProduction()?
                        ApnsClientBuilder.PRODUCTION_APNS_HOST:
                        ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                .setSigningKey(ApnsSigningKey.loadFromPkcs8File(
                        config.getKeyPath().getFile(),
                        config.getTeamId(),
                        config.getKeyId()
                ))
                .setConcurrentConnections(config.getMaxConcurrentStreams())
                .setMetricsListener(new LogginMetricsListener()) // 监控埋点
                .setEventLoopGroup(eventLoopGroup)
                .build();
    }
}

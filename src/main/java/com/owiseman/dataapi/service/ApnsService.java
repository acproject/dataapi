package com.owiseman.dataapi.service;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.repository.SysDeviceTokenRepository;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.owiseman.dataapi.aop.audit.LoggingMetricsListener;
import io.netty.channel.nio.NioEventLoopGroup;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Service
public class ApnsService {
    private static final Logger log = LoggerFactory.getLogger(ApnsService.class);
    
    @Autowired
    private SysUserConfigRepository userConfigRepository;
    
    @Autowired
    private SysDeviceTokenRepository tokenRepository;

    private static final int MAX_RETRIES = 3;
    private static final long INITIAL_BACKOFF_MS = 1000;
    private static int count = 0;

    @Async("pushTaskExecutor")
    public CompletableFuture<Void> sendPush(String apikey, String deviceToken, String message) {
        if (!tokenRepository.isValidToken(deviceToken)) {
            log.warn("无效设备令牌: {}", deviceToken);
            return CompletableFuture.failedFuture(new InvalidTokenException());
        }

        try {
            // 获取用户配置
            SysUserConfig config = userConfigRepository.findByProjectApiKey(apikey)
                .orElseThrow(() -> new RuntimeException("配置不存在"));

            // 创建 APNs 客户端，添加更多配置和错误处理
            ApnsClient apnsClient = new ApnsClientBuilder()
                .setApnsServer(config.getApnsProduction() ? 
                    ApnsClientBuilder.PRODUCTION_APNS_HOST : 
                    ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                .setSigningKey(ApnsSigningKey.loadFromPkcs8File(
                    new File(config.getApnsKeyPath()),
                    config.getApnsTeamId(),
                    config.getApnsKeyId()))
                .setConcurrentConnections(10)
                .setEventLoopGroup(new NioEventLoopGroup(4))
                .setMetricsListener(new LoggingMetricsListener())
                .build();

            final String payload = new SimpleApnsPayloadBuilder()
                .setAlertBody(message)
                .setSound("default")
                .setBadgeNumber(1)
                .build();

            final SimpleApnsPushNotification notification = new SimpleApnsPushNotification(
                deviceToken,
                config.getApnsBundleId(),
                payload);

            final PushNotificationResponse<SimpleApnsPushNotification> response =
                apnsClient.sendNotification(notification).get();

            handleResponse(response, message);
            return CompletableFuture.completedFuture(null);
        } catch (Exception e) {
            log.error("推送执行失败", e);
            return CompletableFuture.failedFuture(e);
        }
    }

    private void handleResponse(PushNotificationResponse<SimpleApnsPushNotification> response, String message) {
        if (response.isAccepted()) {
            log.info("推送成功: {}", response.getPushNotification().getToken());
        } else {
            log.error("推送失败: {}", response.getRejectionReason());
            handleFailure(response, message);
        }
    }

    private void handleFailure(PushNotificationResponse<SimpleApnsPushNotification> response,String message) {
        final String token = response.getPushNotification().getToken();

        // 处理失效令牌
        if (response.getTokenInvalidationTimestamp() != null) {
            tokenRepository.invalidateToken(token);
            log.warn("标记为失效令牌: {}", token);
        }

        // 重试逻辑（示例）
        if (shouldRetry(String.valueOf(response.getRejectionReason()))) {
            log.info("准备重试: {}", token);
            retryPush(token, message);
        }
    }

    private boolean shouldRetry(String reason) {
        return !reason.contains("BadDeviceToken");
    }

    private void retryPush(String token, String message) {
        // 实现重试逻辑
        if (count < MAX_RETRIES) {
            try {
                Thread.sleep(INITIAL_BACKOFF_MS * (1 << count));
                // 这里需要传入 userId，但我们没有，所以需要修改方法签名和调用方式
                handleFailureRetry(token, message);
                count++;
            } catch (InterruptedException e) {
                log.error("重试中断", e);
            }
        }
    }

    private void handleFailureRetry(String token, String message) {
        // 通过 token 查找对应的 userId
        tokenRepository.findUserIdByToken(token)
            .ifPresent(userId -> 
                sendPush(userId, token, message)
            );
    }

    public static class InvalidTokenException extends Exception {
        // 构造函数
        public InvalidTokenException() {
            super("无效设备令牌");
        }
    }

}

package com.owiseman.dataapi.service;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;

import com.owiseman.dataapi.config.ApnsConfig;

import com.owiseman.dataapi.repository.SysDeviceTokenRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class ApnsService {

    private static final Logger log = LoggerFactory.getLogger(ApnsService.class);
    @Autowired
    private ApnsConfig apnsConfig;
    @Autowired
    private ApnsClient apnsClient;
    @Autowired
    SysDeviceTokenRepository tokenRepository; // 令牌管理依赖

    private static final int MAX_RETRIES = 3; // 最大重试次数
    private static final long INITIAL_BACKOFF_MS = 1000; // 初始退避时间（毫秒）
    private static int count = 0;

    @Async("pushTaskExecutor") // 使用独立线程池
    public CompletableFuture<Void> sendPush(String deviceToken, String message) {
        // 验证令牌有效性
        if (!tokenRepository.isValidToken(deviceToken)) {
            log.warn("无效设备令牌: {}", deviceToken);
            return CompletableFuture.failedFuture(new InvalidTokenException());
        }

        try {
            final String payload = new SimpleApnsPayloadBuilder()
                .setAlertBody(message)
                .setSound("default")
                .setBadgeNumber(1)
                .build();

            final SimpleApnsPushNotification notification = new SimpleApnsPushNotification(
                deviceToken,
                apnsConfig.getBundleId(),
                payload);

            final PushNotificationResponse<SimpleApnsPushNotification> response =
                apnsClient.sendNotification(notification).get();

            handleResponse(response, message);
            return CompletableFuture.completedFuture(null);
        } catch (InterruptedException | ExecutionException e) {
            log.error("推送执行中断", e);
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

    private void retryPush(String token,String message) {
        // 实现重试逻辑
        if (count < MAX_RETRIES) {
            try {
                Thread.sleep(INITIAL_BACKOFF_MS * (1 << count));
                sendPush(token, message);
                count++;
            } catch (InterruptedException e) {
                log.error("重试中断", e);
            }
        }
    }

    public static class InvalidTokenException extends Exception {
        // 构造函数
        public InvalidTokenException() {
            super("无效设备令牌");
        }
    }

}

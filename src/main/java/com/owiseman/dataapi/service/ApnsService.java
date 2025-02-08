package com.owiseman.dataapi.service;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.PushNotificationResponse;
import com.eatthepath.pushy.apns.auth.ApnsSigningKey;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.util.concurrent.FutureCallback;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.owiseman.dataapi.config.ApnsConfig;

import jakarta.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ApnsService {

    private static final Logger log = LoggerFactory.getLogger(ApnsService.class);
    @Autowired
    private ApnsConfig apnsConfig;
    private ApnsClient apnsClient;
    @PostConstruct
    public void init() {
        try {
            apnsClient = new ApnsClientBuilder()
                    .setApnsServer(apnsConfig.isProduction()?
                            ApnsClientBuilder.PRODUCTION_APNS_HOST:
                            ApnsClientBuilder.DEVELOPMENT_APNS_HOST)
                    .setSigningKey(ApnsSigningKey.loadFromPkcs8File(
                            apnsConfig.getKeyPath().getFile(),
                            apnsConfig.getTeamId(),
                            apnsConfig.getKeyId()
                    )).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPush(String deviceToken, String message) throws JsonProcessingException {
        ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder() {
            @Override
            public String build() {
                return "";
            }

            @Override
            public String buildMdmPayload(String s) {
                return "";
            }
        };

        payloadBuilder.setAlertBody(message);
        payloadBuilder.setSound("default");
        payloadBuilder.setBadgeNumber(1);

        String payload = payloadBuilder.build();

        SimpleApnsPushNotification push = new SimpleApnsPushNotification(
                deviceToken,
                apnsConfig.getBundleId(),
                payload
        );
        Future<PushNotificationResponse<SimpleApnsPushNotification>> future
                = apnsClient.sendNotification(push);

        Futures.addCallback((ListenableFuture<? extends Object>) future, new FutureCallback() {
            @Override
            public void onSuccess(Object result) {
               onSuccess(result);
            }

            public void onSuccess(PushNotificationResponse<SimpleApnsPushNotification> result) {
                if (result.isAccepted()) {
                    log.info("推送成功");
                } else {
                    log.info("推送失败");
                }
            }

            @Override
            public void onFailure(Throwable t) {
                log.error("APNs推送异常", t);
            }
        }, Executors.newCachedThreadPool());

    }

}

package com.owiseman.dataapi.service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class FcmService {
    private static final Logger log = LoggerFactory.getLogger(FcmService.class);
    
    @Autowired
    private SysUserConfigRepository userConfigRepository;
    
    // 缓存已初始化的 Firebase 应用
    private final Map<String, FirebaseApp> firebaseApps = new HashMap<>();

    public void sendPush(String userId, String deviceToken, String title, String body) {
        try {
            // 获取用户配置
            SysUserConfig config = userConfigRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("用户配置不存在"));

            // 获取或初始化 Firebase 应用
            FirebaseApp app = getOrInitializeFirebaseApp(config);

            Message message = Message.builder()
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setToken(deviceToken)
                    .build();

            FirebaseMessaging.getInstance(app).send(message);
        } catch (Exception e) {
            log.error("FCM推送失败", e);
            throw new RuntimeException("推送失败", e);
        }
    }

    private FirebaseApp getOrInitializeFirebaseApp(SysUserConfig config) throws IOException {
        String appName = config.getUserId();
        if (firebaseApps.containsKey(appName)) {
            return firebaseApps.get(appName);
        }

        synchronized (firebaseApps) {
            if (firebaseApps.containsKey(appName)) {
                return firebaseApps.get(appName);
            }

            FileInputStream serviceAccount = 
                new FileInputStream(config.getFirebaseServiceAccountPath());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(config.getFirebaseProjectId())
                    .build();

            FirebaseApp app = FirebaseApp.initializeApp(options, appName);
            firebaseApps.put(appName, app);
            return app;
        }
    }
}

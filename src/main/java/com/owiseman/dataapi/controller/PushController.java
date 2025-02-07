package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.PushRequest;
import com.owiseman.dataapi.service.ApnsService;
import com.owiseman.dataapi.service.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/system/push")
public class PushController {
    @Autowired
    private ApnsService apnsService;

    @Autowired
    private FcmService fcmService;

    @PostMapping
    public ResponseEntity<String> sendPush(@RequestBody PushRequest request) {
        try {
            switch (request.getPlatform()) {
                case IOS:
                    apnsService.sendPush(request.getDeviceToken(), request.getContent());
                    break;
                case ANDROID:
                    fcmService.sendPush(request.getDeviceToken(), request.getTitle(), request.getContent());
                    break;
            }
            return ResponseEntity.ok("推送已发送");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("推送失败: " + e.getMessage());
        }
    }

}

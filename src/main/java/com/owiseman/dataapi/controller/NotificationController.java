package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.service.ApnsService;
import com.owiseman.dataapi.service.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    
    @Autowired
    private ApnsService apnsService;
    
    @Autowired
    private FcmService fcmService;

    @PostMapping("/apns")
    public ResponseEntity<Void> sendApnsNotification(
            @RequestParam String userId,
            @RequestParam String deviceToken,
            @RequestParam String message) {
        apnsService.sendPush(userId, deviceToken, message);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/fcm")
    public ResponseEntity<Void> sendFcmNotification(
            @RequestParam String userId,
            @RequestParam String deviceToken,
            @RequestParam String title,
            @RequestParam String body) {
        fcmService.sendPush(userId, deviceToken, title, body);
        return ResponseEntity.ok().build();
    }
}
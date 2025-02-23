package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.RegisterDto;
import com.owiseman.dataapi.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto) {
        registerService.register(registerDto);
        return ResponseEntity.ok().body(Map.of("message", "注册成功"));
    }
}
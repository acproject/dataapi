package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.LoginDto;
import com.owiseman.dataapi.service.LoginService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto) {
        String token = loginService.login(loginDto);
        return ResponseEntity.ok(Map.of(
            "token", token,
            "message", "登录成功"
        ));
    }
}
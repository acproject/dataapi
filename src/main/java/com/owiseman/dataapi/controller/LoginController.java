package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.LoginDto;
import com.owiseman.dataapi.dto.LoginResponseDto;
import com.owiseman.dataapi.dto.NormLoginDto;
import com.owiseman.dataapi.dto.TokenResponse;
import com.owiseman.dataapi.entity.SysUser;
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
        var loginResponseDto  = loginService.login(loginDto);
        return ResponseEntity.ok(Map.of(
            "data", loginResponseDto,
            "message", "登录成功"
        ));
    }

    /**
     * 客户端可以通过管理那里获得realm， 后面封装进SDK中
     * @param normLoginDto
     * @return
     */
    @PostMapping("/norm_user_login")
    public ResponseEntity<?> normUserLogin(@Valid @RequestBody NormLoginDto normLoginDto) {
          var loginResponse = loginService.normUserLogin(normLoginDto);
          LoginResponseDto loginResponseDto = new LoginResponseDto();
           loginResponseDto.setTokenResponse(loginResponse);
        return ResponseEntity.ok(Map.of(
            "data", loginResponseDto,
            "message", "登录成功"
        ));
    }
}
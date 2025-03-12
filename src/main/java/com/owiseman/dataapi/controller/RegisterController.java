package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.NormSysUserDto;
import com.owiseman.dataapi.dto.RegisterDto;
import com.owiseman.dataapi.service.RegisterService;
import com.owiseman.dataapi.util.HttpHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/register")
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 企业注册（组织注册）使用
     * @param registerDto
     * @param httpServletRequest
     * @return
     */
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterDto registerDto,
                                      HttpServletRequest httpServletRequest) {
        String token = HttpHeaderUtil.getTokenFromHeader(httpServletRequest);
        registerService.register(registerDto, token);
        return ResponseEntity.ok().body(Map.of("message", "注册成功"));
    }

    /**
     * 为组织的项目提供的注册服务，必须提供项目的id,也是提供给SDK调用的
     * @param normSysUserDto
     * @param httpServletRequest
     * @return
     */
    @PostMapping("/norm")
    public ResponseEntity<?> normRegister(@RequestBody NormSysUserDto normSysUserDto,
                                          HttpServletRequest httpServletRequest) {
        String token = HttpHeaderUtil.getTokenFromHeader(httpServletRequest);
        if (normSysUserDto.getProjectId().isEmpty() || normSysUserDto.getProjectId() == null) {
            throw new RuntimeException("项目id不能为空");
        }
        if (normSysUserDto.getRealmName().isEmpty() || normSysUserDto.getRealmName() == null) {
            throw new RuntimeException("realm不能为空");
        }
        if (normSysUserDto.getUsername().isEmpty() || normSysUserDto.getUsername() == null) {
            throw new RuntimeException("用户名不能为空");
        }
        if (normSysUserDto.getPassword().isEmpty() || normSysUserDto.getPassword() == null) {
            throw new RuntimeException("秘密不能为空");
        }
        if (normSysUserDto.getId() == null || normSysUserDto.getId().isEmpty()) {
            normSysUserDto.setId(UUID.randomUUID().toString());
        }
        registerService.normRegister(normSysUserDto, token);
        return ResponseEntity.ok().body(Map.of("message", "注册成功"));

    }

}
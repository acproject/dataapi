package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.LoginDto;
import com.owiseman.dataapi.dto.LoginResponseDto;
import com.owiseman.dataapi.dto.NormLoginDto;
import com.owiseman.dataapi.dto.TokenResponse;
import com.owiseman.dataapi.entity.SysUser;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import com.owiseman.dataapi.repository.SysUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);
    @Autowired
    private KeycloakTokenService keycloakTokenService;

    @Autowired
    private SysUserRepository sysUserRepository;

    @Autowired
    private SysUserConfigRepository sysUserConfigRepository;

    public LoginResponseDto login(LoginDto loginDto) {
        // 判断是用户名还是邮箱登录
        SysUser user;
        if (loginDto.principal().contains("@")) {
            user = sysUserRepository.findByEmail(loginDto.principal())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        } else {
            user = sysUserRepository.findByUsername(loginDto.principal())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        }

        // 验证用户状态
        if (!user.getEnabled()) {
            throw new RuntimeException("用户已被禁用");
        }

      //   使用对应realm的配置进行认证
        TokenResponse token = keycloakTokenService.getTokenByUsernameAndPassword(
                loginDto.principal(),
                loginDto.password(),
                user.getRealmName()
        );

        // 更新登录信息
        updateUserAuthInfo(user);
        LoginResponseDto loginResponseDto = new LoginResponseDto();
        loginResponseDto.setTokenResponse(token);
        return loginResponseDto;
    }

    public TokenResponse normUserLogin(NormLoginDto normLoginDto) {
        // 判断是用户名还是邮箱登录
        SysUser user;
        if (normLoginDto.principal().contains("@")) {
            user = sysUserRepository.findByEmail(normLoginDto.principal())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        } else {
            user = sysUserRepository.findByUsername(normLoginDto.principal())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));
        }

        // 验证用户状态
        if (!user.getEnabled()) {
            throw new RuntimeException("用户已被禁用");
        }

        // 使用对应realm的配置进行认证
        TokenResponse token = keycloakTokenService.getTokenByUsernameAndPassword(
                normLoginDto.principal(),
                normLoginDto.password(),
                normLoginDto.projectId()
        );

        return token;
    }

    private void updateUserAuthInfo(SysUser user) {
        // 更新最后登录时间
        Map<String, List<String>> attributes = user.getAttributes();
        if (attributes == null) {
            attributes = new HashMap<>();
        }

        // 记录最后登录时间
        attributes.put("lastLoginTime", List.of(String.valueOf(System.currentTimeMillis())));

        // 更新登录次数
        List<String> loginCountList = attributes.getOrDefault("loginCount", List.of("0"));
        int loginCount = Integer.parseInt(loginCountList.get(0)) + 1;
        attributes.put("loginCount", List.of(String.valueOf(loginCount)));

        user.setAttributes(attributes);
        sysUserRepository.update(user);
    }
}
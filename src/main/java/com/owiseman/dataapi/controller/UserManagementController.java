package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.NormSysUserDto;
import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.dto.ProjectDto;
import com.owiseman.dataapi.dto.UserUpdateDto;
import com.owiseman.dataapi.entity.SysUser;
import com.owiseman.dataapi.service.RegisterService;
import com.owiseman.dataapi.service.UserManagementService;
import com.owiseman.dataapi.util.HttpHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;
    // 这里使用registerService的NormRegister来代替CreateUser
    @Autowired
    private RegisterService registerService;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody NormSysUserDto normSysUserDto, HttpServletRequest servletRequest) {
        String token =  HttpHeaderUtil.getTokenFromHeader(servletRequest);
        registerService.normRegister(normSysUserDto, token);
        return ResponseEntity.ok().body(Map.of("message", "成功创建用户"));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable String userId,HttpServletRequest servletRequest) {
        String token =  HttpHeaderUtil.getTokenFromHeader(servletRequest);
        userManagementService.deleteNormUser(userId, token);
        return ResponseEntity.ok().body(Map.of("message", "成功删除用户"));
    }

    @PostMapping("/list")
    public ResponseEntity<PageResult<SysUser>> listUsers(
            @RequestBody ProjectDto projectDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size, HttpServletRequest servletRequest) {
        String token =  HttpHeaderUtil.getTokenFromHeader(servletRequest);
        return ResponseEntity.ok(userManagementService.listUsers(projectDto, page, size));
    }

    @PutMapping("/{userId}/profile")
    public ResponseEntity<?> updateProfile(
            @PathVariable String userId,
            @RequestBody NormSysUserDto updateDto,
            HttpServletRequest httpServletRequest) {
        String token =  HttpHeaderUtil.getTokenFromHeader(httpServletRequest);
        userManagementService.updateUserProfile(userId, updateDto,token);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{userId}/reset-password")
    public ResponseEntity<?> resetPassword(
            @PathVariable String userId,
            @RequestBody String newPassword, HttpServletRequest httpServletRequest) {
        String token =  HttpHeaderUtil.getTokenFromHeader(httpServletRequest);
        userManagementService.resetPassword(userId, newPassword);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{userId}/avatar")
    public ResponseEntity<?> updateAvatar(
            @PathVariable String userId,
            @RequestBody String avatarUrl, HttpServletRequest httpServletRequest) {
        String token =  HttpHeaderUtil.getTokenFromHeader(httpServletRequest);
        userManagementService.updateAvatar(userId, avatarUrl, token);
        return ResponseEntity.ok().build();
    }
}
package com.owiseman.dataapi.dto;

public record UserAttributesDto(
    String avatar,           // 头像URL
    String phone,           // 电话号码
    String position,        // 职位
    String department,      // 部门
    String location,        // 地理位置
    String timezone,        // 时区
    String language,        // 首选语言
    String theme,           // UI主题偏好
    Boolean emailNotifications, // 邮件通知开关
    Boolean smsNotifications   // 短信通知开关
) {}
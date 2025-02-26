package com.owiseman.dataapi.dto;

public enum Platform {
    IOS("iOS应用"),
    ANDROID("安卓应用"),
    WEB("Web应用"),
    WECHAT_MINI("微信小程序"),
    ALIPAY_MINI("支付宝小程序"),
    DESKTOP("桌面应用"),
    EMBEDDING("嵌入式应用"),
    OTHER("其他");
    private final String description;

    Platform(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

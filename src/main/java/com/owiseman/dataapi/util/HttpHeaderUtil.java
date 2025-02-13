package com.owiseman.dataapi.util;

import jakarta.servlet.http.HttpServletRequest;

public class HttpHeaderUtil {
    public static String getTokenFromHeader(HttpServletRequest servletRequest) {
         String authHeader = servletRequest.getHeader("Authorization");
        String token = "";
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }
        return token;
    }
}

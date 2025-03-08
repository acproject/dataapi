package com.owiseman.dataapi.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注意：此工具类无法验证token的有效性
 */
public class JwtParserUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 解析 JWT Token 字符串，返回头部和载荷信息
     */
    public static Map<String, Object> parseJwt(String token) {
        if (token == null || token.split("\\.").length != 3) {
            throw new IllegalArgumentException("Invalid JWT Token");
        }

        String[] parts = token.split("\\.");
        Map<String, Object> result = new HashMap<>();

        // 解析 Header
        String headerJson = new String(Base64.getUrlDecoder().decode(parts[0]));
        result.put("header", parseJsonToMap(headerJson));

        // 解析 Payload
        String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
        result.put("payload", parseJsonToMap(payloadJson));

        return result;
    }

    /**
     * 从 Token 直接获取用户ID（sub）
     */
    public static String getUserId(String token) {
        return (String) ((Map<String, Object>) parseJwt(token).get("payload")).get("sub");
    }

    public static String getIssUrl(String token) {
        return (String) ((Map<String, Object>) parseJwt(token).get("payload")).get("iss");
    }

    public static String getClientId(String token) {
        return (String) ((Map<String, Object>) parseJwt(token).get("payload")).get("azp");
    }

    public static String getEmail(String token) {
        return (String) ((Map<String, Object>) parseJwt(token).get("payload")).get("email");
    }

    public static String getRealmName(String token) {
        String iss =  (String) ((Map<String, Object>) parseJwt(token).get("payload")).get("iss");
        return iss.substring(iss.lastIndexOf("/") + 1);
    }
    /**
     * 获取用户名（preferred_username）
     */
    public static String getUsername(String token) {
        return (String) ((Map<String, Object>) parseJwt(token).get("payload")).get("preferred_username");
    }

    /**
     * 获取领域角色列表
     */
    public static List<String> getRealmRoles(String token) {
        Map<String, Object> payload = (Map<String, Object>) parseJwt(token).get("payload");
        Map<String, Object> realmAccess = payload != null ? (Map<String, Object>) payload.get("realm_access") : null;
        return realmAccess != null ? (List<String>) realmAccess.get("roles") : List.of();
    }

    /**
     * 获取所有声明（Claims）
     */
    public static Map<String, Object> getAllClaims(String token) {
        return (Map<String, Object>) parseJwt(token).get("payload");
    }

    // 辅助方法：JSON 字符串转 Map
    private static Map<String, Object> parseJsonToMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }

//    public static void main(String[] args) {
//        String token = "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJTMDdfU29Fa0NoTnlSUTNCaEJ1eENiM1JJeXpROUtJUndNOEVDdHV4N3dnIn0.eyJleHAiOjE3NDE0MTMxMTIsImlhdCI6MTc0MTQxMjgxMiwianRpIjoiNjJhNmNiZjMtNjI5Ny00ZTEwLTgyM2QtOThkYTYyZDY1MjYxIiwiaXNzIjoiaHR0cDovL2xvY2FsaG9zdDo5MDgwL3JlYWxtcy9oeF9sYWIxMzQiLCJhdWQiOiJhY2NvdW50Iiwic3ViIjoiZGI5ZWEyMDgtYTFiYi00OTg4LTkyNjgtNGM0YjQwOTRjZGFjIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiaHhfbGFiMTM0LWFkbWluLWNsaWVudCIsInNpZCI6ImM1YWY3OTEwLTkxZTAtNDhlYS04MzQ5LWEzNjk3YjE4OTcwZSIsImFjciI6IjEiLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsiZGVmYXVsdC1yb2xlcy1oeF9sYWIxMzQiLCJvZmZsaW5lX2FjY2VzcyIsImFkbWluIiwidW1hX2F1dGhvcml6YXRpb24iXX0sInJlc291cmNlX2FjY2VzcyI6eyJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJkZW5nIGRlbmciLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJoeF9sYWIxMzQiLCJnaXZlbl9uYW1lIjoiZGVuZyIsImZhbWlseV9uYW1lIjoiZGVuZyIsImVtYWlsIjoiaHhfbGFiMTM0QHFxcS5jb20ifQ.EaISsuQXJ0KOJieiZz8klobddeQsmDRxqwz0SjhH7h3oguXlmNuPNOChSLnF0ZyGePen4xGPkVjVkHguYyNiCrASEKNfvArtV3UBaeod_yEeKR4b2Iac9WfcPNULz027IvU_2_yDbPqBiZPOk_LTYSROB1AZ7wHlHtgbNb_i9ftUp69DLvYRz-HAlR92DQQE7fAwtHHMzQ-az_fXskWRXv9gQL6a3hmr5C7yfrrzLE1Ikg51oHL4jAHRFpnZCDnl39JGt7AJXcvGRhuI9AWPsfA_mDArIiQwQaOso-sTx-TiNvnQS07Z48VEUN2w2wGQKLmE_ag1kYfZdFG-tky4mg";
//        String result = getClientId(token);
//        System.out.println(result);
//    }
}
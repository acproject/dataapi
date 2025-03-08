package com.owiseman.dataapi.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 这个类可以利用Keycloak的Token进行解析，获取用户信息，如用户名、角色等。
 * 并且可以验证签名是否有效
 * 但是需要填写iss地址，例如： http://localhost:9080/realms/myrealm
 * 通过 该类的初始化将iss 地址传入，即可解析出用户信息。
 */
public class KeycloakTokenUtils {
    // 需要提前初始化 JwtDecoder
    private static JwtDecoder jwtDecoder;

     /**
     * 初始化 JwtDecoder（需要在应用启动时配置）
     */
    public static void init(String issuerUri) {
        jwtDecoder = JwtDecoders.fromIssuerLocation(issuerUri);
    }

     /**
     * 解析原始 JWT Token 字符串
     */
    public static Jwt parseToken(String token) {
        if (!StringUtils.hasText(token)) {
            throw new IllegalArgumentException("Token cannot be empty");
        }

        try {
            // 去除可能的 "Bearer " 前缀
            String cleanedToken = token.replaceFirst("^Bearer\\s+", "");
            return jwtDecoder.decode(cleanedToken);
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT token: " + e.getMessage(), e);
        }
    }

    /**
     * 获取原始 JWT 对象
     */
    public static Jwt getJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Assert.isInstanceOf(JwtAuthenticationToken.class, authentication,
                "Authentication must be JwtAuthenticationToken");
        return ((JwtAuthenticationToken) authentication).getToken();
    }

    /**
     * 获取用户唯一标识（sub claim）
     */
    public static String getUserId() {
        return getJwt().getSubject();
    }

    /**
     * 获取用户名（preferred_username）
     */
    public static String getUsername() {
        return getClaimAsString("preferred_username");
    }

    /**
     * 获取用户邮箱
     */
    public static String getEmail() {
        return getClaimAsString("email");
    }

    /**
     * 获取客户端ID（clientId）
     */
    public static String getClientId() {
        return getClaimAsString("azp");
    }

    /**
     * 获取领域角色列表
     */
    public static List<String> getRealmRoles() {
        Map<String, Object> realmAccess = getClaimAsMap("realm_access");
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            return (List<String>) realmAccess.get("roles");
        }
        return Collections.emptyList();
    }

    /**
     * 获取客户端角色列表
     * @param clientId 客户端ID（默认当前客户端）
     */
    public static List<String> getClientRoles(String clientId) {
        Map<String, Object> resourceAccess = getClaimAsMap("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey(clientId)) {
            Map<String, Object> clientAccess = (Map<String, Object>) resourceAccess.get(clientId);
            if (clientAccess.containsKey("roles")) {
                return (List<String>) clientAccess.get("roles");
            }
        }
        return Collections.emptyList();
    }

    /**
     * 获取自定义声明值
     * @param claimName 声明名称
     * @return 字符串类型值（自动转换）
     */
    public static String getClaimAsString(String claimName) {
        return Optional.ofNullable(getJwt().getClaimAsString(claimName))
                .orElse("");
    }

    /**
     * 获取自定义声明值
     * @param claimName 声明名称
     * @return Map类型值（自动转换）
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getClaimAsMap(String claimName) {
        return getJwt().hasClaim(claimName) ?
                (Map<String, Object>) getJwt().getClaim(claimName) :
                Collections.emptyMap();
    }

    /**
     * 验证 Token 是否包含指定角色
     * @param role 角色名称
     */
    public static boolean hasRealmRole(String role) {
        return getRealmRoles().contains(role);
    }

    /**
     * 验证 Token 是否包含客户端指定角色
     * @param clientId 客户端ID
     * @param role 角色名称
     */
    public static boolean hasClientRole(String clientId, String role) {
        return getClientRoles(clientId).contains(role);
    }

    /**
     * 获取所有声明（Claims）
     */
    public static Map<String, Object> getAllClaims() {
        return getJwt().getClaims();
    }

}

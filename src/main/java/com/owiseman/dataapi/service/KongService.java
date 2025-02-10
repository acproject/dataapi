package com.owiseman.dataapi.service;

import com.owiseman.dataapi.router.RegisterServiceRequest;
import com.owiseman.dataapi.router.RouteRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// todo: 解决在不同服务中注册同名的路由报错的问题
@Service
public class KongService {
    @Value("${kong.admin.url}")
    private String kongAdminUrl;

    /**
     * {
     * "name": "my-service001",
     * "url": "http://192.168.2.4:8080/api/query"  // 直接暴露给kong真实地址
     * }
     *
     * @param registerServiceRequest
     */
    public void registerService(RegisterServiceRequest registerServiceRequest) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestServiceBody = new HashMap<>();
        requestServiceBody.put("name", registerServiceRequest.getName());
        requestServiceBody.put("url", registerServiceRequest.getUrl());
        HttpEntity<Map<String, String>> requestEntity =
                new HttpEntity<>(requestServiceBody, headers);
        ResponseEntity<String> response =
                restTemplate.postForEntity(kongAdminUrl + "/services",
                        requestEntity, String.class);
    }

    /**
     * {
     * "name": "users",
     * "paths": ["/api/users"],                 // ["{paths}/{name}] 由前端拼接完成，所以这个参数不用传递
     * "strip_path": true,                      // 注意这里一定要为true, 所以这个参数不用传递
     * "methods": ["POST", "PUT", "DELETE"],
     * "protocols": ["http", "https"],
     * "service": "my-service001"
     * }
     * 注册完成后：就可以通过注册的路由http://localhost:8000/api/users去访问
     *
     * @param routeRequest
     */
    public void registerRoute(RouteRequest routeRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> requestRouteBody = new HashMap<>();
        requestRouteBody.put("name", routeRequest.getName());
        requestRouteBody.put("strip_path", true);
        requestRouteBody.put("methods", routeRequest.getMethods());
        requestRouteBody.put("protocols", routeRequest.getProtocols());
        String serviceName = routeRequest.getService();
        // 增加自己的注册逻辑
        List<String> paths = null;
        // 判断是否是admin，如果是admin角色，则需要增加admin路径
        if (!routeRequest.getAdmin()) {
            paths = Arrays.asList("/" + serviceName + "/api/" + routeRequest.getName());
        } else {
            paths = Arrays.asList("/" + serviceName + "/admin/api/" + routeRequest.getName());
        }
        requestRouteBody.put("paths", paths);
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestRouteBody, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(kongAdminUrl
                        + "/services/"
                        + serviceName
                        + "/routes",
                requestEntity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to register route: " + response.getBody());
        }
    }

    public void unregisterRoute(String routeNameOrId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.delete(kongAdminUrl + "/routes/" + routeNameOrId);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Route not found: " + routeNameOrId);
            } else {
                throw new RuntimeException("Failed to unregister route: " + e.getMessage());
            }
        }
    }

    public void unregisterService(String serviceNameOrId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.delete(kongAdminUrl + "/services/" + serviceNameOrId);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Service not found: " + serviceNameOrId);
            } else {
                throw new RuntimeException("Failed to unregister service: " + e.getMessage());
            }
        }
    }

    public ResponseEntity<String> getServices(String offset, Integer size) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            StringBuilder urlBuilder = new StringBuilder(kongAdminUrl + "/services");
            if (size != null || offset != null) {
                urlBuilder.append("?");
                if (size > 0 && size != null) {
                    urlBuilder.append("size=").append(size);
                } else {
                    size = 1;
                    urlBuilder.append("size=").append(size);
                }
                if (offset != null) {
                    if (size != null) {
                        urlBuilder.append("&");
                    }
                    urlBuilder.append("offset=").append(offset);
                }
            }
            return restTemplate.exchange(urlBuilder.toString(),
                    HttpMethod.GET, HttpEntity.EMPTY, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<String> getRoutes(String offset, Integer size) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            StringBuilder urlBuilder = new StringBuilder(kongAdminUrl + "/routes");
            if (size != null || offset != null) {
                urlBuilder.append("?");
                if (size > 0 && size != null) {
                    urlBuilder.append("size=").append(size);
                } else {
                    size = 1;
                    urlBuilder.append("size=").append(size);
                }
                if (offset != null) {
                    if (size != null) {
                        urlBuilder.append("&");
                    }
                    urlBuilder.append("offset=").append(offset);
                }
            }
            return restTemplate.exchange(urlBuilder.toString(),
                    HttpMethod.GET, HttpEntity.EMPTY, String.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

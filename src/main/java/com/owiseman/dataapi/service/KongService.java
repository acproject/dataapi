package com.owiseman.dataapi.service;

import com.owiseman.dataapi.Router.RouteRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Service
public class KongService {
    @Value("${kong.admin.url}")
    private String kongAdminUrl;

    public void registerRoute(RouteRequest routeRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("id", routeRequest.getId());
        requestBody.put("name", routeRequest.getName());
        requestBody.put("uris", routeRequest.getUris());
        requestBody.put("methods", routeRequest.getMethods());
        requestBody.put("upstream_url", routeRequest.getUpstreamUrl());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(kongAdminUrl + "/routes",
                requestEntity, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Failed to register route: " + response.getBody());
        }
    }

    public void unregisterRoute(String routeNameOrId) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            restTemplate.delete(kongAdminUrl + "/routes/" + routeNameOrId);
        }catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new RuntimeException("Route not found: " + routeNameOrId);
            } else {
                throw new RuntimeException("Failed to unregister route: " + e.getMessage());
            }
        }
    }
}

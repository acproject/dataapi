package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.ProjectCreateRequest;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<SysUserConfig> createProject(
            @Valid @RequestBody ProjectCreateRequest request,
            @RequestHeader("Authorization") String token,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(projectService.createProject(userId, request, token));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<SysUserConfig> getProjectDetails(
            @PathVariable String projectId,
            Authentication authentication) {
        String userId = authentication.getName();
        SysUserConfig config = projectService.getProjectDetails(userId, projectId);
        
        // 隐藏敏感信息
        config.setKeycloakAuthUrl(null);
        config.setKeycloakTokenUrl(null);
        
        return ResponseEntity.ok(config);
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<SysUserConfig> updateProject(
            @PathVariable String projectId,
            @Valid @RequestBody SysUserConfig updateRequest,
            Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(projectService.updateProject(userId, projectId, updateRequest));
    }
}
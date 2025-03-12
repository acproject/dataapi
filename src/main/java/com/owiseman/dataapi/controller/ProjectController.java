package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.ProjectApiKeyDto;
import com.owiseman.dataapi.dto.ProjectCreateRequest;
import com.owiseman.dataapi.dto.ProjectDto;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.service.ProjectService;
import com.owiseman.dataapi.util.HttpHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<SysUserConfig> createProject(
            @RequestBody SysUserConfig request, HttpServletRequest servletRequest) {
        String token = HttpHeaderUtil.getTokenFromHeader(servletRequest);

        return ResponseEntity.ok(projectService.createProject(request));
    }

    /**
     * 通用的一个方法，SDK也可以使用
     * @return
     */
    @PostMapping("/useKey")
    public ResponseEntity<SysUserConfig> getProjectWithKey(@RequestBody ProjectApiKeyDto projectApiKeyDto) {
        return ResponseEntity.ok(projectService.getProjectDetailsByApiKey(projectApiKeyDto.key()));
    }

    @GetMapping("/list/{userId}")
    public ResponseEntity<List<SysUserConfig>> getProjects(@PathVariable String userId) {
        return ResponseEntity.ok(projectService.getProjects(userId));
    }

    @PostMapping("/details")
    public ResponseEntity<SysUserConfig> getProjectDetails(
            @RequestBody ProjectDto projectDto) {
        SysUserConfig config = projectService.getProjectDetails(projectDto.getUserId(), projectDto.getId());

        // 隐藏敏感信息
        config.setKeycloakAuthUrl(null);
        config.setKeycloakTokenUrl(null);

        return ResponseEntity.ok(config);
    }

    @PutMapping("/update/{projectId}")
    public ResponseEntity<SysUserConfig> updateProject(
            @PathVariable String projectId, @RequestBody SysUserConfig updateRequest) {
        return ResponseEntity.ok(projectService.updateProject(updateRequest.getUserId(), projectId, updateRequest));
    }
}
package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.CreateKeycloakClientDto;
import com.owiseman.dataapi.dto.KeycloakClientDto;
import com.owiseman.dataapi.dto.ProjectCreateRequest;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import com.owiseman.dataapi.repository.SysUserRepository;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
public class ProjectService {
    @Autowired
    SysUserRepository sysUserRepository;


    private final SysUserConfigRepository userConfigRepository;

    public ProjectService(KeycloakClientService keycloakClientService,
                          SysUserConfigRepository userConfigRepository) {

        this.userConfigRepository = userConfigRepository;
    }

    @Transactional
    public SysUserConfig createProject(String userId, ProjectCreateRequest request, String token) {
        // 检查项目名称是否已存在
        if (userConfigRepository.existsByProjectName(request.projectName())) {
            throw new RuntimeException("项目名称已存在");
        }


        var user = sysUserRepository.findById(userId);
        if (user.isEmpty()) {
            throw new RuntimeException("用户不存在");
        }
        // 为用户创建一个项目

        SysUserConfig config = new SysUserConfig.Builder()
                .id(UUID.randomUUID().toString())
                .projectName(request.projectName())
                .platform(request.platform())
                .projectApiKey(UUID.randomUUID().toString().replaceAll("-",""))
                .build();

        return userConfigRepository.save(config);
    }

    public SysUserConfig getProjectDetails(String userId, String projectId) {
        return userConfigRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("项目不存在或无权访问"));
    }

    @Transactional
    public SysUserConfig updateProject(String userId, String projectId, SysUserConfig updateRequest) {
        SysUserConfig existingConfig = userConfigRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("项目不存在或无权访问"));

        // 保护不可修改的字段
        updateRequest.setId(existingConfig.getId());
        updateRequest.setUserId(existingConfig.getUserId());
        updateRequest.setKeycloakRealm(existingConfig.getKeycloakRealm());
        updateRequest.setKeycloakClientId(existingConfig.getKeycloakClientId());
        updateRequest.setKeycloakClientSecret(existingConfig.getKeycloakClientSecret());
        updateRequest.setKeycloakAuthUrl(existingConfig.getKeycloakAuthUrl());
        updateRequest.setKeycloakTokenUrl(existingConfig.getKeycloakTokenUrl());

        return userConfigRepository.save(updateRequest);
    }
}
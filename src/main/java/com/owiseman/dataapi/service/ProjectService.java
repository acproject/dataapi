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



    private final KeycloakClientService keycloakClientService;
    private final SysUserConfigRepository userConfigRepository;

    public ProjectService(KeycloakClientService keycloakClientService,
                         SysUserConfigRepository userConfigRepository) {
        this.keycloakClientService = keycloakClientService;
        this.userConfigRepository = userConfigRepository;
    }

    @Transactional
    public SysUserConfig createProject(String userId, ProjectCreateRequest request, String token) {
        // 检查项目名称是否已存在
        if (userConfigRepository.existsByKeycloakClientId(request.projectName())) {
            throw new RuntimeException("项目名称已存在");
        }

        // 创建 Keycloak 客户端
        ClientRepresentation clientRepresentation = new ClientRepresentation();
        clientRepresentation.setClientId(request.projectName());
        clientRepresentation.setName(request.projectName());
        clientRepresentation.setDescription("Platform: " + request.platform().getDescription());
        clientRepresentation.setEnabled(true);
        clientRepresentation.setDirectAccessGrantsEnabled(true);
        clientRepresentation.setStandardFlowEnabled(true);
        clientRepresentation.setPublicClient(false);
        clientRepresentation.setRedirectUris(Collections.singletonList("*"));
        CreateKeycloakClientDto createKeycloakClientDto = new CreateKeycloakClientDto();
        createKeycloakClientDto.setClientRepresentation(clientRepresentation);
        // 通过 userId查找的realmName，clientID，client_secret, username
       var user = sysUserRepository.findById(userId);
        createKeycloakClientDto.setRealmName(user.get().getRealmName());
        createKeycloakClientDto.setClientId(user.get().getClientId());
        createKeycloakClientDto.setUsername(user.get().getUsername());
        var clientSecret= userConfigRepository.findByUserId(userId).get().getKeycloakClientSecret();
        createKeycloakClientDto.setClientSecret(clientSecret);
        KeycloakClientDto clientDto = keycloakClientService.createClient(createKeycloakClientDto, token);

        // 创建用户配置
        SysUserConfig config = new SysUserConfig();
        config.setId(UUID.randomUUID().toString());
        config.setProjectName(request.projectName());
        config.setPlatform(request.platform());
        config.setUserId(userId);
        config.setKeycloakClientId(clientDto.clientId());
        config.setKeycloakClientSecret(clientDto.secret());
        config.setStorageType("seaweedfs"); // 默认存储类型
        
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
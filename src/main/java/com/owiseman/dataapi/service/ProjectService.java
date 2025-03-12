package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.CreateKeycloakClientDto;
import com.owiseman.dataapi.dto.KeycloakClientDto;
import com.owiseman.dataapi.dto.ProjectCreateRequest;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import com.owiseman.dataapi.repository.SysUserRepository;
import com.owiseman.dataapi.util.JwtParserUtil;
import org.keycloak.representations.idm.ClientRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {
    @Autowired
    SysUserRepository sysUserRepository;

    @Value("${keycloak.urls.auth}")
    String authUrl;

    private final SysUserConfigRepository userConfigRepository;

    public ProjectService(KeycloakClientService keycloakClientService,
                          SysUserConfigRepository userConfigRepository) {

        this.userConfigRepository = userConfigRepository;
    }

    @Transactional
    public SysUserConfig createProject(SysUserConfig request) {
        // 检查项目名称是否已存在
        String realmName = sysUserRepository.findById(request.getUserId()).get().getRealmName();
        if (userConfigRepository.existsByProjectName(request.getProjectName(), realmName)) {
            if (realmName == null || realmName.isEmpty()) {
                throw new RuntimeException("用户未登录");
            }
            throw new RuntimeException("项目名称已存在");
        }

        request.setKeycloakRealm(realmName);
        // 为用户创建一个项目
        SysUserConfig config = request;
        config.setId(UUID.randomUUID().toString());
        config.setProjectName(request.getProjectName());
        config.setPlatform(request.getPlatform());
        config.setKeycloakTokenUrl(authUrl);
        config.setDatabaseTableNamePrefix(request.getProjectName()
                + "_"
                + UUID.randomUUID().toString().substring(0, 4)
                + "_");
        config.setProjectApiKey(UUID.randomUUID().toString().replaceAll("-", ""));

        return userConfigRepository.save(config);
    }

    public SysUserConfig getProjectDetailsByApiKey(String apiKey) {
        return userConfigRepository.findByProjectApiKey(apiKey)
                .orElseThrow(() -> new RuntimeException("项目不存在或无权访问"));
    }

    public SysUserConfig getProjectDetails(String userId, String projectId) {
        return userConfigRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("项目不存在或无权访问"));
    }

    public List<SysUserConfig> getProjects(String userId) {
        String realmName = sysUserRepository.findById(userId).get().getRealmName();
        if (realmName == null || realmName.isEmpty()) {
            throw new RuntimeException("用户未登录");
        }
        return userConfigRepository.findAll(realmName);
    }

    @Transactional
    public SysUserConfig updateProject(String userId, String projectId, SysUserConfig updateRequest) {
        SysUserConfig existingConfig = userConfigRepository.findByIdAndUserId(projectId, userId)
                .orElseThrow(() -> new RuntimeException("项目不存在或无权访问"));


        return userConfigRepository.update(updateRequest);
    }
}
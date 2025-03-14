package com.owiseman.dataapi.service;

import com.owiseman.dataapi.entity.SysUserFile;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileService {
    SysUserFile uploadFile(String userId, MultipartFile file, Optional<String> parentId, String projectApiKey);
    Resource downloadFile(String userId, String fid);
    Resource downloadFile(String fid);
}

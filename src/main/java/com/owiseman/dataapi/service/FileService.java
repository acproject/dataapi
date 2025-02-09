package com.owiseman.dataapi.service;

import com.owiseman.dataapi.dto.AssignResponse;
import com.owiseman.dataapi.entity.SysUserFile;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    SysUserFile uploadFile(String userId, MultipartFile file);
    Resource downloadFile(String userId, String fid);
}

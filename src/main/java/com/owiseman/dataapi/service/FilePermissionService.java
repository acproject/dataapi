package com.owiseman.dataapi.service;

import com.owiseman.dataapi.entity.SysFilePermission;
import com.owiseman.dataapi.repository.SysFilePermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FilePermissionService {
    @Autowired
    private SysFilePermissionRepository filePermissionRepository;

    @Transactional
    public void grantPermission(String fileId, String userId, boolean read, boolean write, boolean delete, boolean share) {
        SysFilePermission permission = new SysFilePermission();
        permission.setFileId(fileId);
        permission.setUserId(userId);
        permission.setCanRead(read);
        permission.setCanWrite(write);
        permission.setCanDelete(delete);
        permission.setCanShare(share);

        filePermissionRepository.save(permission);
    }

    public boolean canRead(String fileId, String userId) {
        return filePermissionRepository.findByFileIdAndUserId(fileId, userId)
            .map(SysFilePermission::isCanRead)
            .orElse(false);
    }

    public boolean canWrite(String fileId, String userId) {
        return filePermissionRepository.findByFileIdAndUserId(fileId, userId)
            .map(SysFilePermission::isCanWrite)
            .orElse(false);
    }

    public boolean canDelete(String fileId, String userId) {
        return filePermissionRepository.findByFileIdAndUserId(fileId, userId)
            .map(SysFilePermission::isCanDelete)
            .orElse(false);
    }
}
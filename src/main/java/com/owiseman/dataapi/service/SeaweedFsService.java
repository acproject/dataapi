package com.owiseman.dataapi.service;

import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.repository.SysUserFilesRepository;
import com.owiseman.dataapi.service.storage.ObjectStorageService;
import com.owiseman.dataapi.service.storage.StorageServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SeaweedFsService implements FileService {
    @Autowired
    private StorageServiceFactory storageFactory;
    
    @Autowired
    private SysUserFilesRepository sysUserFilesRepository;

    @Value("${storage.default-type:seaweedfs}")
    private String defaultStorageType;

    @Override
    @Transactional
    public SysUserFile uploadFile(String userId, MultipartFile file, Optional<String> parentId) {
        try {
            ObjectStorageService storageService = storageFactory.getService(defaultStorageType);
            String fileId = storageService.upload(file);
            
            SysUserFile meta = new SysUserFile();
            meta.setId(UUID.randomUUID().toString());
            meta.setUserId(userId);
            meta.setFid(fileId);
            meta.setFileName(file.getOriginalFilename());
            meta.setSize(file.getSize());
            meta.setUploadTime(LocalDateTime.now());
            meta.setDirectory(false);
            meta.setStorageType(defaultStorageType);
            
            // 设置父目录和路径
            if (parentId.isPresent()) {
                SysUserFile parentDir = sysUserFilesRepository.findByIdAndUserId(parentId.get(), userId)
                        .orElseThrow(() -> new RuntimeException("父目录不存在"));
                meta.setParentId(parentId.get());
                meta.setPath(parentDir.getPath() + "/" + file.getOriginalFilename());
            } else {
                // 如果没有父目录，则放在用户根目录下
                SysUserFile rootDir = sysUserFilesRepository.findRootDirectoryByUserId(userId)
                        .orElseThrow(() -> new RuntimeException("用户根目录不存在"));
                meta.setParentId(rootDir.getId());
                meta.setPath(rootDir.getPath() + "/" + file.getOriginalFilename());
            }
            
            return sysUserFilesRepository.save(meta);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败", e);
        }
    }

    @Override
    public Resource downloadFile(String userId, String fileId) {
        try {
            SysUserFile file = sysUserFilesRepository.findByIdAndUserId(fileId, userId)
                .orElseThrow(() -> new FileNotFoundException("文件不存在或无权访问"));
            
            ObjectStorageService storageService = storageFactory.getService(file.getStorageType());
            return storageService.download(file.getFid());
        } catch (IOException e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }

    @Transactional
    public SysUserFile createDirectory(String userId, String dirName, String parentId) {
        SysUserFile directory = new SysUserFile();
        directory.setId(UUID.randomUUID().toString());
        directory.setUserId(userId);
        directory.setFileName(dirName);
        directory.setFid("dir_" + UUID.randomUUID().toString());
        directory.setSize(0L);
        directory.setUploadTime(LocalDateTime.now());
        directory.setDirectory(true);
        directory.setStorageType(defaultStorageType);
        
        if (parentId != null) {
            SysUserFile parentDir = sysUserFilesRepository.findByIdAndUserId(parentId, userId)
                    .orElseThrow(() -> new RuntimeException("父目录不存在"));
            directory.setParentId(parentId);
            directory.setPath(parentDir.getPath() + "/" + dirName);
        } else {
            SysUserFile rootDir = sysUserFilesRepository.findRootDirectoryByUserId(userId)
                    .orElseThrow(() -> new RuntimeException("用户根目录不存在"));
            directory.setParentId(rootDir.getId());
            directory.setPath(rootDir.getPath() + "/" + dirName);
        }
        
        return sysUserFilesRepository.save(directory);
    }

    @Transactional
    public void deleteFile(String userId, String fileId) {
        try {
            SysUserFile file = sysUserFilesRepository.findByIdAndUserId(fileId, userId)
                    .orElseThrow(() -> new FileNotFoundException("文件不存在或无权访问"));
            
            if (!file.isDirectory()) {
                ObjectStorageService storageService = storageFactory.getService(file.getStorageType());
                storageService.delete(file.getFid());
            }
            
            sysUserFilesRepository.deleteByIdAndUserId(fileId, userId);
        } catch (IOException e) {
            throw new RuntimeException("文件删除失败", e);
        }
    }

    private String getDefaultStorageType() {
        return defaultStorageType;
    }
}

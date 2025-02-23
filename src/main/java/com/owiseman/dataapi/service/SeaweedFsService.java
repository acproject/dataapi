package com.owiseman.dataapi.service;

import com.owiseman.dataapi.config.SeaweedFSClient;
import com.owiseman.dataapi.dto.AssignResponse;
import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.repository.SysUserFilesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class SeaweedFsService implements FileService{

    @Autowired
    private SeaweedFSClient seaweedFSClient;
    @Autowired
    private SysUserFilesRepository sysUserFilesRepository;

    @Override
    @Transactional
    public SysUserFile uploadFile(String userId, MultipartFile file, Optional<String> parentId) {
        AssignResponse assign = seaweedFSClient.assign();
        try {
            String fid = seaweedFSClient.upload(assign, file);
            SysUserFile meta = new SysUserFile();
            meta.setUserId(userId);
            meta.setFid(fid);
            meta.setFileName(file.getOriginalFilename());
            meta.setSize(file.getSize());
            meta.setUploadTime(LocalDateTime.now());
            meta.setDirectory(false);
            
            // 设置父目录和路径
            if (parentId != null) {
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
            throw new RuntimeException(e);
        }
    }

    // 添加创建目录的方法
    @Transactional
    public SysUserFile createDirectory(String userId, String dirName, String parentId) {
        SysUserFile directory = new SysUserFile();
        directory.setUserId(userId);
        directory.setFileName(dirName);
        directory.setFid("dir_" + UUID.randomUUID().toString()); // 目录使用特殊的 fid 前缀
        directory.setSize(0L);
        directory.setUploadTime(LocalDateTime.now());
        directory.setDirectory(true);
        
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

    @Override
    public Resource downloadFile(String userId, String fid) {
        SysUserFile file = null;
        try {
            file = sysUserFilesRepository.findByIdAndUserId(fid, userId)
                    .orElseThrow(() -> new FileNotFoundException("File not found or access denied"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String volumeUrl = seaweedFSClient.getVolumeUrl(file.getFid());
        String downloadUrl = "http://" + volumeUrl + "/" + file.getFid();
        // 获取文件流
        ResponseEntity<Resource> response = new RestTemplate()
                .getForEntity(downloadUrl, Resource.class);

        // 设置下载文件名
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFileName() + "\"");
        try {
            return new InputStreamResource(response.getBody().getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void deleteFile(String userId, String fid) {
        try {
            // 从数据库获取要删除的数据
            SysUserFile file = sysUserFilesRepository.findByIdAndUserId(fid, userId)
                    .orElseThrow(() -> new FileNotFoundException("File not found or access denied"));
            // 删除SeaweedFS文件
            deleteFromSeaweedFS(file.getFid());
            // 删除数据库记录
            sysUserFilesRepository.deleteByIdAndUserId(fid, userId);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
     private void deleteFromSeaweedFS(String fid) {
            String volumeUrl = seaweedFSClient.getVolumeUrl(fid);
            String deleteUrl = "http://" + volumeUrl + "/" + fid;
            new RestTemplate().delete(deleteUrl);
        }
}

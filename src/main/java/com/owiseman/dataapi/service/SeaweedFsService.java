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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;

public class SeaweedFsService implements FileService{

    @Autowired
    private SeaweedFSClient seaweedFSClient;
    @Autowired
    private SysUserFilesRepository sysUserFilesRepository;

    @Override
    @Transactional
    public SysUserFile uploadFile(String userId, MultipartFile file) {
        AssignResponse assign = seaweedFSClient.assign();
        try {
            String fid = seaweedFSClient.upload(assign, file);
            SysUserFile meta = new SysUserFile();
            meta.setUserId(userId);
            meta.setFid(fid);
            meta.setFileName(file.getOriginalFilename());
            meta.setSize(file.getSize());
            meta.setUploadTime(LocalDateTime.now());
            return sysUserFilesRepository.save(meta);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
}

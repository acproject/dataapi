package com.owiseman.dataapi.service.storage;

import com.owiseman.dataapi.config.SeaweedFSClient;
import com.owiseman.dataapi.dto.AssignResponse;
import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.service.SeaweedFsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class SeaweedFSStorageService implements ObjectStorageService {
    private final SeaweedFSClient seaweedFSClient;

    @Autowired
    public SeaweedFSStorageService(SeaweedFSClient seaweedFSClient) {

        this.seaweedFSClient = seaweedFSClient;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        AssignResponse assign = seaweedFSClient.assign();
        return seaweedFSClient.upload(assign, file);
    }

    @Override
    public Resource download(String fileId) throws IOException {
        String url = "http://" + seaweedFSClient.getVolumeUrl(fileId) + "/" + fileId;
        return new UrlResource(url);
    }

    @Override
    public void delete(String fileId) throws IOException {
        String volumeUrl = seaweedFSClient.getVolumeUrl(fileId);
        String deleteUrl = "http://" + volumeUrl + "/" + fileId;
        // 执行删除操作
    }

    @Override
    public String getFileUrl(String fileId) {
        return "http://" + seaweedFSClient.getVolumeUrl(fileId) + "/" + fileId;
    }

    @Override
    public String upload(String userId, MultipartFile file) throws IOException {
        return "";
    }

    @Override
    public Resource download(String userId, String fileId) throws IOException {
        return null;
    }

    @Override
    public void delete(String userId, String fileId) {

    }

    @Override
    public String getFileUrl(String userId, String fileId) {
        return "";
    }

    @Override
    public String getStorageType() {
        return "seaweedfs";
    }

    @Override
    public void createDirectory(String userId, String path) throws IOException {
        // 1. 路径规范化：确保以斜杠结尾
        if (!path.endsWith("/")) {
            path += "/";
        }

        // 2. 分配存储位置
        AssignResponse assign = seaweedFSClient.assign();

        // 3. 上传空文件模拟目录
        try (ByteArrayInputStream emptyStream = new ByteArrayInputStream(new byte[0])) {
            seaweedFSClient.upload(assign, emptyStream, path);
        } catch (Exception e) {
            throw new IOException("Failed to create directory: " + path, e);
        }

    }
}
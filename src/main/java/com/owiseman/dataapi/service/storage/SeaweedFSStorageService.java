package com.owiseman.dataapi.service.storage;

import com.owiseman.dataapi.config.SeaweedFSClient;
import com.owiseman.dataapi.dto.AssignResponse;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class SeaweedFSStorageService implements ObjectStorageService {
    private final SeaweedFSClient seaweedFSClient;

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
}
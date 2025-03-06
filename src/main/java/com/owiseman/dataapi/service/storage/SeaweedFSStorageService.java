package com.owiseman.dataapi.service.storage;

import com.owiseman.dataapi.config.SeaweedFSClient;
import com.owiseman.dataapi.util.FileTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class SeaweedFSStorageService implements ObjectStorageService {
    private final SeaweedFSClient seaweedFSClient;

    @Autowired
    public SeaweedFSStorageService(SeaweedFSClient seaweedFSClient) {

        this.seaweedFSClient = seaweedFSClient;
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        return seaweedFSClient.upload(file, seaweedFSClient.getMasterUrl(), seaweedFSClient.getValUrl() ,"/root");
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
    public String upload(String userId, MultipartFile file, Optional<String> parentId) throws IOException {
        return FileTypeUtil.createDirectoryViaHttpFile(file, parentId.orElse("/tmp"), seaweedFSClient.getMasterUrl(), seaweedFSClient.getValUrl());
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
        return StorageType.seaweedfs.getType();
    }

    @Override
    public void createDirectory(String UserId, String path) throws IOException {
        FileTypeUtil.createDirectoryViaHttp(path, seaweedFSClient.getMasterUrl(), seaweedFSClient.getValUrl());
    }


}
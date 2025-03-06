package com.owiseman.dataapi.service.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface ObjectStorageService {
    String upload(MultipartFile file) throws IOException;
    Resource download(String fileId) throws IOException;
    void delete(String fileId) throws IOException;
    String getFileUrl(String fileId);

    String upload(String userId, MultipartFile file, Optional<String> parentId) throws IOException;

    Resource download(String userId, String fileId) throws IOException;

    void delete(String userId, String fileId);

    String getFileUrl(String userId, String fileId);

    String getStorageType();
    void createDirectory(String userId, String path) throws IOException;
}
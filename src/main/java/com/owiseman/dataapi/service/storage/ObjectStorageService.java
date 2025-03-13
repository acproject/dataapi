package com.owiseman.dataapi.service.storage;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUserFile;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
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

    PageResult<SysUserFile> pageFiles(String userId, String parentId, int pageNumber, int pageSize);

    String getStorageType();
    void createDirectory(String userId, String path) throws IOException;
}
package com.owiseman.dataapi.service.storage;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AliyunOSSStorageService implements ObjectStorageService {
    private final SysUserConfigRepository userConfigRepository;
    private final Map<String, OSS> ossClientCache = new ConcurrentHashMap<>();

    public AliyunOSSStorageService(SysUserConfigRepository userConfigRepository) {
        this.userConfigRepository = userConfigRepository;
    }

    private OSS getOssClient(String userId) {
        return ossClientCache.computeIfAbsent(userId, this::createOssClient);
    }

    private OSS createOssClient(String userId) {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        return new OSSClientBuilder().build(
            config.getOssEndpoint(),
            config.getOssAccessKeyId(),
            config.getOssAccessKeySecret()
        );
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        throw new UnsupportedOperationException("请使用带有userId的方法");
    }

    @Override
    public Resource download(String fileId) throws IOException {
        throw new UnsupportedOperationException("请使用带有userId的方法");
    }

    @Override
    public void delete(String fileId) throws IOException {
        throw new UnsupportedOperationException("请使用带有userId的方法");
    }

    @Override
    public String getFileUrl(String fileId) {
        throw new UnsupportedOperationException("请使用带有userId的方法");
    }

    @Override
    public String upload(String userId, MultipartFile file, Optional<String> parentId) throws IOException {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        String key = generateKey(file);
        getOssClient(userId).putObject(config.getOssBucketName(), key, file.getInputStream());
        return key;
    }

    @Override
    public Resource download(String userId, String fileId) throws IOException {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        OSSObject object = getOssClient(userId).getObject(config.getOssBucketName(), fileId);
        return new InputStreamResource(object.getObjectContent());
    }

    @Override
    public void delete(String userId, String fileId) {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        getOssClient(userId).deleteObject(config.getOssBucketName(), fileId);
    }

    @Override
    public String getFileUrl(String userId, String fileId) {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        return getOssClient(userId).generatePresignedUrl(
            config.getOssBucketName(),
            fileId,
            new Date(System.currentTimeMillis() + 3600 * 1000)
        ).toString();
    }

    @Override
    public String getStorageType() {
        return StorageType.aliyun.getType();
    }

   @Override
    public void createDirectory(String userId, String path) {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        // Ensure the path ends with a slash
        if (!path.endsWith("/")) {
            path += "/";
        }

        // Create a zero-length object to simulate a directory
        getOssClient(userId).putObject(config.getOssBucketName(), path, new ByteArrayInputStream(new byte[0]));
    }


    private String generateKey(MultipartFile file) {
        return System.currentTimeMillis() + "_" + file.getOriginalFilename();
    }
}
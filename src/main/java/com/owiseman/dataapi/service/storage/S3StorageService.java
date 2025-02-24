package com.owiseman.dataapi.service.storage;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class S3StorageService implements ObjectStorageService {
    private final SysUserConfigRepository userConfigRepository;
    private final Map<String, AmazonS3> s3ClientCache = new ConcurrentHashMap<>();

    public S3StorageService(SysUserConfigRepository userConfigRepository) {
        this.userConfigRepository = userConfigRepository;
    }

    private AmazonS3 getS3Client(String userId) {
        return s3ClientCache.computeIfAbsent(userId, this::createS3Client);
    }

    private AmazonS3 createS3Client(String userId) {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        BasicAWSCredentials credentials = new BasicAWSCredentials(
            config.getS3AccessKey(), 
            config.getS3SecretKey()
        );

        return AmazonS3ClientBuilder.standard()
            .withRegion(config.getS3Region())
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .build();
    }

    @Override
    public String upload(MultipartFile file) throws IOException {
        return "";
    }

    @Override
    public Resource download(String fileId) throws IOException {
        return null;
    }

    @Override
    public void delete(String fileId) throws IOException {

    }

    @Override
    public String getFileUrl(String fileId) {
        return "";
    }

    @Override
    public String upload(String userId, MultipartFile file) throws IOException {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        String key = generateKey(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());
        
        getS3Client(userId).putObject(
            config.getS3BucketName(), 
            key, 
            file.getInputStream(), 
            metadata
        );
        return key;
    }

    @Override
    public Resource download(String userId, String fileId) throws IOException {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        S3Object object = getS3Client(userId).getObject(config.getS3BucketName(), fileId);
        return new InputStreamResource(object.getObjectContent());
    }

    @Override
    public void delete(String userId, String fileId) {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        getS3Client(userId).deleteObject(config.getS3BucketName(), fileId);
    }

    @Override
    public String getFileUrl(String userId, String fileId) {
        SysUserConfig config = userConfigRepository.findByUserId(userId)
            .orElseThrow(() -> new RuntimeException("用户配置不存在"));

        return getS3Client(userId).getUrl(config.getS3BucketName(), fileId).toString();
    }

    @Override
    public String getStorageType() {
        return "s3";
    }

    private String generateKey(MultipartFile file) {
        return System.currentTimeMillis() + "_" + file.getOriginalFilename();
    }
}
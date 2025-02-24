package com.owiseman.dataapi.service.storage;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StorageServiceFactory {
    private final Map<String, ObjectStorageService> services = new ConcurrentHashMap<>();

    public StorageServiceFactory(SeaweedFSStorageService seaweedFSService,
                               S3StorageService s3Service,
                               AliyunOSSStorageService aliyunService) {
        services.put(seaweedFSService.getStorageType(), seaweedFSService);
        services.put(s3Service.getStorageType(), s3Service);
        services.put(aliyunService.getStorageType(), aliyunService);
    }

    public ObjectStorageService getService(String type) {
        return services.get(type);
    }
}
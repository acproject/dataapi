package com.owiseman.dataapi.service.storage;

public enum StorageType {
    s3("s3"),
    seaweedfs("seaweedfs"),
    aliyun("aliyun");

    private final String type;
    StorageType(String type) {
        this.type = type;
    }
    public String getType() {
        return type;
    }
}

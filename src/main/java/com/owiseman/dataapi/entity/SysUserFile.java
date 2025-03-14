package com.owiseman.dataapi.entity;


import com.owiseman.dataapi.service.storage.StorageType;
import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "sys_user_files", indexes = {
        @Index(name = "idx_parent_id", columnList = "parent_id"),
        @Index(name = "idx_path", columnList = "path")
})
public class SysUserFile {
    @Id
    @GeneratedValue(generator = "uuid")
    @Convert(converter = UUIDConverter.class)
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name ="fid", nullable = false, length = 255)
    private String fid;
    @Column(name ="file_name",nullable = false, length = 255)
    private String fileName;
    @Column(name ="size")
    private Long size;
    @Column(name ="upload_time")
    private LocalDateTime uploadTime;
    @Column(name = "project_id")
    private String projectId;

    @Column(name = "project_api_key")
    private String projectApiKey;

    public String getProjectApiKey() {
        return projectApiKey;
    }

    public void setProjectApiKey(String projectApiKey) {
        this.projectApiKey = projectApiKey;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(LocalDateTime uploadTime) {
        this.uploadTime = uploadTime;
    }



    public SysUserFile() {
    }

    @Column(name = "parent_id", nullable = true) // 允许为空（根目录没有父目录
    private String parentId;

    @Column(name = "path", length = 1024)
    private String path;

    @Column(name = "is_directory", nullable = false, columnDefinition = "boolean default false")
    private Boolean isDirectory = false;

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        if (path != null) {
            // 统一路径格式为以 / 开头且不以 / 结尾
            this.path = path.replaceAll("/+", "/")
                    .replaceAll("^/?", "/")
                    .replaceAll("/$", "");
        } else {
            this.path = null;
        }
    }

    public Boolean getDirectory() {
        return isDirectory;
    }

    public void setDirectory(Boolean directory) {
        isDirectory = directory;
    }

    public SysUserFile(String id, String userId, String fid, String fileName, Long size, LocalDateTime uploadTime, String projectId, String parentId, String path, Boolean isDirectory, String storageType, String projectApiKey) {
        this.id = id;
        this.userId = userId;
        this.fid = fid;
        this.fileName = fileName;
        this.size = size;
        this.uploadTime = uploadTime;
        this.projectId = projectId;
        this.parentId = parentId;
        this.path = path;
        this.isDirectory = isDirectory;
        this.storageType = storageType;
        this.projectApiKey = projectApiKey;
    }

    @Column(name = "storage_type", nullable = false)
    private String storageType = StorageType.seaweedfs.getType();

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public boolean isDirectory() {
        return getDirectory();
    }

    public static class Builder {
        private String id;
        private String userId;
        private String fid;
        private String fileName;
        private Long size;
        private LocalDateTime uploadTime;
        private String parentId;
        private String path;
        private Boolean isDirectory = false;
        private String storageType;
        private String projectId;
        private String projectApiKey;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder fid(String fid) {
            this.fid = fid;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder size(Long size) {
            this.size = size;
            return this;
        }

        public Builder uploadTime(LocalDateTime uploadTime) {
            this.uploadTime = uploadTime;
            return this;
        }

        public Builder parentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public Builder isDirectory(Boolean isDirectory) {
            this.isDirectory = isDirectory;
            return this;
        }

        public Builder storageType(String storageType) {
            this.storageType = storageType;
            return this;
        }

        public Builder projectId(String projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder projectApiKey(String projectApiKey) {
            this.projectApiKey = projectApiKey;
            return this;
        }

        public SysUserFile build() {
            SysUserFile userFile = new SysUserFile();
            userFile.setId(id);
            userFile.setUserId(userId);
            userFile.setFid(fid);
            userFile.setFileName(fileName);
            userFile.setSize(size);
            userFile.setUploadTime(uploadTime);
            userFile.setParentId(parentId);
            userFile.setPath(path);
            userFile.setDirectory(isDirectory);
            userFile.setStorageType(storageType);
            userFile.setProjectId(projectId);
            userFile.setProjectApiKey(projectApiKey);
            return userFile;
        }
    }
}

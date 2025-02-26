package com.owiseman.dataapi.entity;


import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;
import org.checkerframework.checker.units.qual.C;

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
    @Column(nullable = false, length = 64)
    private String fid;
    @Column(nullable = false, length = 255)
    private String fileName;
    private Long size;
    private LocalDateTime uploadTime;

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

    public SysUserFile(String userId, String fid, String fileName, Long size, LocalDateTime uploadTime) {
        this.userId = userId;
        this.fid = fid;
        this.fileName = fileName;
        this.size = size;
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

    public SysUserFile(String id, String userId, String fid, String fileName, Long size, LocalDateTime uploadTime, String parentId, String path, Boolean isDirectory) {
        this.id = id;
        this.userId = userId;
        this.fid = fid;
        this.fileName = fileName;
        this.size = size;
        this.uploadTime = uploadTime;
        this.parentId = parentId;
        this.path = path;
        this.isDirectory = isDirectory;
    }

    @Column(name = "storage_type", nullable = false)
    private String storageType;

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
            return userFile;
        }
    }
}

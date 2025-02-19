package com.owiseman.dataapi.entity;


import com.owiseman.dataapi.util.UUIDConverter;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@Entity
@Table(name = "sys_user_files")
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
}

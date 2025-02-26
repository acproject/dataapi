package com.owiseman.dataapi.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sys_file_permissions")
public class SysFilePermission {
    @Id
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(name = "file_id")
    private String fileId;

    @Column(name = "user_id")
    private String userId;

    private boolean canRead;
    private boolean canWrite;
    private boolean canDelete;
    private boolean canShare;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isCanRead() {
        return canRead;
    }

    public void setCanRead(boolean canRead) {
        this.canRead = canRead;
    }

    public boolean isCanWrite() {
        return canWrite;
    }

    public void setCanWrite(boolean canWrite) {
        this.canWrite = canWrite;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }

    public boolean isCanShare() {
        return canShare;
    }

    public void setCanShare(boolean canShare) {
        this.canShare = canShare;
    }

    public SysFilePermission() {
    }

    public SysFilePermission(String id, String fileId, String userId, boolean canRead, boolean canWrite, boolean canDelete, boolean canShare) {
        this.id = id;
        this.fileId = fileId;
        this.userId = userId;
        this.canRead = canRead;
        this.canWrite = canWrite;
        this.canDelete = canDelete;
        this.canShare = canShare;
    }

    public static class Builder {
        private String id;
        private String fileId;
        private String userId;
        private boolean canRead;
        private boolean canWrite;
        private boolean canDelete;
        private boolean canShare;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder fileId(String fileId) {
            this.fileId = fileId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder canRead(boolean canRead) {
            this.canRead = canRead;
            return this;
        }

        public Builder canWrite(boolean canWrite) {
            this.canWrite = canWrite;
            return this;
        }

        public Builder canDelete(boolean canDelete) {
            this.canDelete = canDelete;
            return this;
        }

        public Builder canShare(boolean canShare) {
            this.canShare = canShare;
            return this;
        }

        public SysFilePermission build() {
            SysFilePermission filePermission = new SysFilePermission();
            filePermission.setId(id);
            filePermission.setFileId(fileId);
            filePermission.setUserId(userId);
            filePermission.setCanRead(canRead);
            filePermission.setCanWrite(canWrite);
            filePermission.setCanDelete(canDelete);
            filePermission.setCanShare(canShare);
            return filePermission;
        }
    }
}
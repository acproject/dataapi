package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.dto.PageResult;
import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import com.owiseman.dataapi.service.SeaweedFsService;
import com.owiseman.dataapi.service.storage.StorageServiceFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

/**
 * 该服务更加通用，比如S3，阿里的OSS等
 */
@RestController
@RequestMapping("/api/storage")
@PreAuthorize("hasRole('ADMIN')")
/**
 * 该服务更加通用，比如S3，阿里的OSS等
 */
public class StorageController {

    private final StorageServiceFactory storageFactory;
    private final SysUserConfigRepository userConfigRepository;
    private final SeaweedFsService fileService;

    public StorageController(StorageServiceFactory storageFactory,
                           SysUserConfigRepository userConfigRepository,
                           SeaweedFsService fileService) {
        this.storageFactory = storageFactory;
        this.userConfigRepository = userConfigRepository;
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<SysUserFile> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestBody SysUserFile sysUserFile
            ) throws IOException {

        // 使用统一的文件服务上传
        SysUserFile uploadedFile = fileService.uploadFile(
                sysUserFile.getUserId(),
                file,
                Optional.ofNullable(sysUserFile.getParentId())
        );

        return ResponseEntity.ok(uploadedFile);
    }

    @PostMapping("list")
    public ResponseEntity<PageResult<SysUserFile>> listFiles(
            @RequestBody SysUserFile sysUserFile,
             @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
            ) {
        var pageResult =  storageFactory.getService(sysUserFile.getStorageType())
                .pageFiles(sysUserFile.getUserId(), sysUserFile.getParentId(), page, size);
        return ResponseEntity.ok().body(pageResult);
    }

    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String fileId,
            Authentication authentication) throws IOException {
        
        String userId = authentication.getName();
        Resource file = fileService.downloadFile(userId, fileId);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, 
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable String fileId) throws IOException {

        fileService.deleteFile(fileId);
        
        return ResponseEntity.ok().build();
    }


    @PostMapping("/directory")
    public ResponseEntity<SysUserFile> createDirectory(
            @RequestParam String dirName,
            @RequestParam(required = false) String parentId,
            Authentication authentication) {
        
        String userId = authentication.getName();
        SysUserFile directory = fileService.createDirectory(userId, dirName, parentId);
        
        return ResponseEntity.ok(directory);
    }
}
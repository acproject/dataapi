package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.entity.SysUserConfig;
import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.repository.SysUserConfigRepository;
import com.owiseman.dataapi.service.SeaweedFsService;
import com.owiseman.dataapi.service.storage.StorageServiceFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/api/storage")
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

//    @PostMapping("/upload")
//    public ResponseEntity<SysUserFile> uploadFile(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam(value = "parentId", required = false) String parentId,
//            Authentication authentication) throws IOException {
//
//        String userId = authentication.getName();
//
//
//        // 使用统一的文件服务上传
//        SysUserFile uploadedFile = fileService.uploadFile(
//                userId,
//                file,
//                Optional.ofNullable(parentId)
//        );
//
//        return ResponseEntity.ok(uploadedFile);
//    }

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
            @PathVariable String fileId,
            Authentication authentication) throws IOException {
        
        String userId = authentication.getName();
        fileService.deleteFile(userId, fileId);
        
        return ResponseEntity.ok().build();
    }

//    @GetMapping("/url/{fileId}")
//    public ResponseEntity<String> getFileUrl(
//            @PathVariable String fileId,) {
//
//        SysUserConfig config = userConfigRepository.findByProjectApiKey()
//
//        String url = storageFactory.getService(config.getStorageType())
//                .getFileUrl(userId, fileId);
//
//        return ResponseEntity.ok(url);
//    }

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
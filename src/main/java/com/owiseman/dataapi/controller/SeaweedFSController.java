package com.owiseman.dataapi.controller;

import com.owiseman.dataapi.aop.error.FileExceptionHandler;
import com.owiseman.dataapi.config.SeaweedFSClient;
import com.owiseman.dataapi.entity.SysUserFile;
import com.owiseman.dataapi.repository.SysUserFilesRepository;
import com.owiseman.dataapi.service.SeaweedFsService;
import com.owiseman.dataapi.util.FileTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;

@RestController
@RequestMapping("/api/files")
@PreAuthorize("hasRole('USER')")
public class SeaweedFSController {
    @Autowired
    private SeaweedFsService seaweedFsService;

    @Autowired
    private SeaweedFSClient seaweedFSClient;

    @Autowired
    private SysUserFilesRepository sysUserFilesRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("useId") String userId,
                                        @RequestParam("file") MultipartFile file) {
        try {
            SysUserFile uploadFile = seaweedFsService.uploadFile(userId, file);
            return new ResponseEntity<>(uploadFile, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new FileExceptionHandler()
                    .handleIOError().getStatusCode());
        }
    }

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(@RequestParam("userId") String userId,
                                                 @RequestParam("fid") String fid,
                                                 @RequestParam(required = false) boolean preview
    ) {
        try {
            Resource fileResource = seaweedFsService.downloadFile(userId, fid);
            HttpHeaders headers = new HttpHeaders();
            SysUserFile file = sysUserFilesRepository.findByIdAndUserId(fid, userId).
                    orElseThrow(FileNotFoundException::new);
            if (preview && FileTypeUtil.isPreviewable(file.getFileName())) {
                headers.setContentDisposition(
                        ContentDisposition.inline().filename(file.getFileName()).build());
            } else {
                headers.setContentDisposition(
                        ContentDisposition.attachment().filename(file.getFileName()).build());
            }
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.parseMediaType(getMimeType(file.getFileName())))
                    .body(fileResource);
        } catch (Exception e) {
            return new ResponseEntity<>(new FileExceptionHandler()
                    .handleFileNotFound().getStatusCode());
        }
    }

    @GetMapping("/stream/{userId}/{fileId}")
    public ResponseEntity<Resource> streamFile(
            @PathVariable String userId,
            @PathVariable String fileId,
            @RequestHeader HttpHeaders headers) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        SysUserFile file = sysUserFilesRepository.findByIdAndUserId(fileId, userId).
                orElseThrow(FileNotFoundException::new);
        String volumeUrl = seaweedFSClient.getVolumeUrl(file.getFid());
        String fileUrl = "http://" + volumeUrl + "/" + file.getFid();

        ResponseEntity<Resource> response = restTemplate.exchange(
                RequestEntity.get(URI.create(fileUrl))
                        .headers(headers)
                        .build(),
                Resource.class);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType(response.getHeaders()
                .getContentType().toString()));
        responseHeaders.setContentLength(response.getBody().contentLength());

        if (response.getStatusCode() == HttpStatus.PARTIAL_CONTENT) {
            responseHeaders.set(HttpHeaders.CONTENT_RANGE, response.getHeaders()
                    .getFirst(HttpHeaders.CONTENT_RANGE));
        }
        return new ResponseEntity<>(response.getBody(),
                responseHeaders, HttpStatus.OK);
    }

    @GetMapping("/preview/{userId}/{fileId}")
    public ResponseEntity<Resource> previewFile(
            @PathVariable String userId,
            @PathVariable String fileId
    ) {

        SysUserFile file = null;
        try {
            file = sysUserFilesRepository.findByIdAndUserId(fileId, userId).orElseThrow(
                    FileNotFoundException::new);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String mimeType = FileTypeUtil
                .detectPreviewType(file.getFileName());
//        if (file.getFileName().endsWith(".docx")) {
//            return convertToPdfPreview(file);
        // todo
//        }
        Resource resource = seaweedFsService.downloadFile(userId, fileId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, mimeType)
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{userId}/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable String userId,
                                        @PathVariable String fileId) {
        try {
            seaweedFsService.deleteFile(userId, fileId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new FileExceptionHandler()
                    .handleIOError().getStatusCode());
        }
    }

    private String getMimeType(String fileName) {
        // 简化的MIME类型映射
        return switch (FileTypeUtil.getFileExtension(fileName).toLowerCase()) {
            case "pdf" -> "application/pdf";
            case "png" -> "image/png";
            case "jpg", "jpeg" -> "image/jpeg";
            case "gif" -> "image/gif";
            case "mp4" -> "video/mp4";
            case "webm" -> "video/webm";
            case "ogg" -> "video/ogg";
            case "mp3" -> "audio/mpeg";
            case "wav" -> "audio/wav";
            case "txt" -> "text/plain";
            default -> "application/octet-stream";
        };
    }

//    private ResponseEntity<Resource> convertToPdfPreview(SysUserFile file) {
//        // TODO: 2021/9/23
//        return null;
//    }

}

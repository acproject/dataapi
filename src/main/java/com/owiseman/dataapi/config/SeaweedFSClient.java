package com.owiseman.dataapi.config;

import com.owiseman.dataapi.dto.AssignResponse;
import com.owiseman.dataapi.dto.LookupResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class SeaweedFSClient {
    @Value("${seaweedfs.master.url}")
    private String masterUrl;



    // 获取文件上传分配信息
    public AssignResponse assign() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(masterUrl + "/dir/assign", AssignResponse.class);
    }

    // 上传文件到Volume Server
    public String upload(AssignResponse assign, MultipartFile file) throws IOException {
        String url = "http://" + assign.getUrl() + "/" + assign.getFid();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        HttpEntity<byte[]> entity = new HttpEntity<>(file.getBytes(), headers);
        new RestTemplate().put(url, entity);
        return assign.getFid();
    }

    /**
     * 上传文件到指定路径
     * @param assign 分配信息
     * @param inputStream 文件流
     * @param path 存储路径（包含文件名）
     * @return 文件ID
     */
    public String upload(AssignResponse assign, InputStream inputStream, String path) {
        String uploadUrl = "http://" + assign.getUrl() + "/" + assign.getFid();
        if (path != null && !path.isEmpty()) {
            uploadUrl += "?path=" + URLEncoder.encode(path, StandardCharsets.UTF_8);
        }
        RestTemplate restTemplate = new RestTemplate();

        // 使用 RestTemplate 或其他 HTTP 客户端上传文件
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<InputStream> request = new HttpEntity<>(inputStream, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(uploadUrl, request, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Upload failed: " + response.getBody());
        }
        return assign.getFid();
    }

    public String getVolumeUrl(String fid) {
        // 解析fid格式：volumeId, fileKey -> 例如 "3,01637037d6"
        String[] parts = fid.split(",");
        String volumeId = parts[0];

        RestTemplate rest = new RestTemplate();
        LookupResponse response  = rest.getForObject(masterUrl +
                "/dir/lookup?volumeId=" + volumeId, LookupResponse.class);
        return response.getLocations().get(0).getPublicUrl();
    }
}

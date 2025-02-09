package com.owiseman.dataapi.config;

import com.owiseman.dataapi.dto.AssignResponse;
import com.owiseman.dataapi.dto.LookupResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

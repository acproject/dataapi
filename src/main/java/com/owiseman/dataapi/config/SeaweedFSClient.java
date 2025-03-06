package com.owiseman.dataapi.config;

import com.owiseman.dataapi.dto.LookupResponse;
import com.owiseman.dataapi.util.FileTypeUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Component
public class SeaweedFSClient {
    @Value("${seaweedfs.master.url}")
    private String masterUrl;

    @Value("${seaweedfs.val.url}")
    private String valUrl;


    public String getMasterUrl() {
        return masterUrl;
    }

    public String getValUrl() {
        return valUrl;
    }


    /**
     * 上传文件到指定路径
     *
     * @param file
     * @param path 存储路径（包含文件名）
     * @return 文件ID
     */
    public String upload(MultipartFile file, String masterUrl, String Url, String path) {
        return FileTypeUtil.createDirectoryViaHttpFile(file,path, masterUrl, Url);
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

package com.owiseman.dataapi.util;

import com.owiseman.dataapi.dto.AssignResponse;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileTypeUtil {
    private static final Map<String, String> MINE_TYPES = Map.of("pdf", "application/pdf",
            "png", "image/png",
            "jpg", "image/jpeg",
            "mp4", "video/mp4",
            "txt", "text/plain"
    );

    private static final Set<String> PREVIEWABLE_TYPES = Set.of(
            "pdf", "png", "jpg", "jpeg", "gif",
            "mp4", "webm", "ogg", "mp3", "wav", "txt");

    public static boolean isPreviewable(String fileName) {
        String ext = getFileExtension(fileName).toLowerCase();
        return PREVIEWABLE_TYPES.contains(ext);
    }

    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static String detectPreviewType(String fileName) {
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
        return MINE_TYPES.getOrDefault(ext, "application/octet-stream");
    }


    // 目前SeaweedFS Client不能使用，且它目前不支持创建目录，只能通过上传空文件来模拟目录创建

    /**
     *
     * @param path (包含文件)
     * @param masterHost （master服务地址） = "http://localhost:9333"
     * @param valHost （ seaweedfs vol 服务地址） = "http://localhost:9334"
     */
    public static String createDirectoryViaHttp(String path,String masterHost, String valHost) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        path = path.replaceAll("/+", "/"); // 移除多余斜杠

        // 创建目录标记文件
        String directoryMarker = path.endsWith("/") ?
                path + ".keep" :
                path + "/.keep";

        try {
            // 1. 获取文件ID分配
            AssignResponse assignResponse = assign(masterHost,valHost);

            if (assignResponse == null || assignResponse.getFid() == null) {
                throw new RuntimeException("无法从SeaweedFS获取文件ID");
            }

            // 2. 上传空文件作为目录标记
            String uploadUrl = assignResponse.getServerUrl()+ "/" + assignResponse.getFid();

            // 配置RestTemplate，添加所有必要的消息转换器
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            messageConverters.add(new ResourceHttpMessageConverter());
            // 添加JSON消息转换器
            messageConverters.add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter());
            // 添加字符串消息转换器
            messageConverters.add(new org.springframework.http.converter.StringHttpMessageConverter());
            // 添加表单消息转换器
            messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            
            // 使用directoryMarker中的文件名作为上传文件名
            String fileName = directoryMarker.substring(directoryMarker.lastIndexOf("/") + 1);
            
            ByteArrayResource fileResource = new ByteArrayResource(new byte[0]) {
                @Override
                public String getFilename() {
                    return fileName;
                }
            };

            HttpEntity<Resource> requestEntity = new HttpEntity<>(fileResource, headers);

            // 使用exchange方法而不是postForEntity，可以更好地控制响应类型
            ResponseEntity<Map> response = restTemplate.exchange(
                    uploadUrl,
                    org.springframework.http.HttpMethod.POST,
                    requestEntity,
                    Map.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("上传目录标记文件失败: " + response.getStatusCode());
            }

            // 3. 返回文件URL，包含完整的路径信息
            return "http://" + assignResponse.getServerUrl() + "/" + assignResponse.getFid() + 
                   "?path=" + URLEncoder.encode(path, StandardCharsets.UTF_8) + 
                   "&filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("通过HTTP创建目录失败: " + path + ", 原因: " + e.getMessage(), e);
        }
    }

    public static String createDirectoryViaHttpFile(MultipartFile file, String path, String masterHost, String valHost) {
        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        path = path.replaceAll("/+", "/"); // 移除多余斜杠

        try {
            // 1. 获取文件ID分配
            AssignResponse assignResponse = assign(masterHost, valHost);

            if (assignResponse == null || assignResponse.getFid() == null) {
                throw new RuntimeException("无法从SeaweedFS获取文件ID");
            }

            // 2. 上传文件
            String uploadUrl = assignResponse.getServerUrl() + "/" + assignResponse.getFid();

            // 配置RestTemplate，添加所有必要的消息转换器
            RestTemplate restTemplate = new RestTemplate();
            List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
            messageConverters.add(new ResourceHttpMessageConverter());
            messageConverters.add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter());
            messageConverters.add(new org.springframework.http.converter.StringHttpMessageConverter());
            messageConverters.add(new org.springframework.http.converter.FormHttpMessageConverter());
            restTemplate.setMessageConverters(messageConverters);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            // 使用MultipartFile的内容创建ByteArrayResource
            ByteArrayResource fileResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };

            // 创建MultiValueMap用于表单数据
            org.springframework.util.MultiValueMap<String, Object> body = new org.springframework.util.LinkedMultiValueMap<>();
            body.add("file", fileResource);

            HttpEntity<org.springframework.util.MultiValueMap<String, Object>> requestEntity = 
                new HttpEntity<>(body, headers);

            // 发送请求
            ResponseEntity<Map> response = restTemplate.exchange(
                    uploadUrl,
                    org.springframework.http.HttpMethod.POST,
                    requestEntity,
                    Map.class);

            if (!response.getStatusCode().is2xxSuccessful()) {
                throw new RuntimeException("上传文件失败: " + response.getStatusCode());
            }
            System.out.println(assignResponse.getServerUrl() + "/" + assignResponse.getFid() +
                   "?path=" + URLEncoder.encode(path, StandardCharsets.UTF_8) +
                   "&filename=" + URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8));

            // 3. 返回文件URL，包含完整的路径信息
            return assignResponse.getServerUrl() + "/" + assignResponse.getFid() +
                   "?path=" + URLEncoder.encode(path, StandardCharsets.UTF_8) +
                   "&filename=" + URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("通过HTTP上传文件失败: " + path + ", 原因: " + e.getMessage(), e);
        }
    }

    private static AssignResponse assign(String masterHost,String valHost) {
        // 配置RestTemplate，添加所有必要的消息转换器
        RestTemplate restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        // 添加JSON消息转换器
        messageConverters.add(new org.springframework.http.converter.json.MappingJackson2HttpMessageConverter());
        // 添加字符串消息转换器
        messageConverters.add(new org.springframework.http.converter.StringHttpMessageConverter());
        restTemplate.setMessageConverters(messageConverters);

        var tmp = restTemplate.getForObject(masterHost + "/dir/assign", AssignResponse.class); // master
        var result = new AssignResponse(tmp.getFid(), tmp.getUrl(), tmp.getPublicUrl(), valHost); // 这里要和docker配置一致
        return result;
    }

//    public static void main(String[] args) throws Exception {
//        try {
//            String result = createDirectoryViaHttp("/test-dir3", "http://localhost:9333", "http://localhost:9334");
//            System.out.println("Created directory via HTTP: " + result);
//        } catch (Exception e2) {
//            e2.printStackTrace();
//        }
//    }


}



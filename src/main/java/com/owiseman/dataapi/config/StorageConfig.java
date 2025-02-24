package com.owiseman.dataapi.config;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {
    @Bean
    public AmazonS3 amazonS3Client() {
        return AmazonS3ClientBuilder.standard()
            .withRegion("your-region")
            .build();
    }

    @Bean
    public OSS aliyunOSSClient() {
        return new OSSClientBuilder()
            .build("endpoint", "accessKeyId", "accessKeySecret");
    }
}
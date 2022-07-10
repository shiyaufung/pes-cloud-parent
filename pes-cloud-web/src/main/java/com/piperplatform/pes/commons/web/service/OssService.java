/*
 * Copyright 2017-2022 shiyoufeng.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.piperplatform.pes.commons.web.service;

import cn.hutool.core.util.StrUtil;
import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Object storage service implementation
 *
 * @author shiyoufeng
 * @since 2022-05-14
 */
@Service
public class OssService {

    @Value("${application.oss.public-url}")
    private String publicUrl;

    @Value("${application.oss.local-url}")
    private String localUrl;

    @Value("${application.oss.path}")
    private String path;

    @Autowired
    private MinioClient minioClient;


    @Bean
    public MinioClient minioClient(@Value("${application.oss.local-url}") String localUrl,
                                   @Value("${application.oss.access-key}") String accessKey,
                                   @Value("${application.oss.secret-key}") String secretKey) throws Exception {
        return MinioClient.builder()
                .endpoint(localUrl)
                .credentials(accessKey, secretKey)
                .build();
    }

    public ObjectWriteResponse upload(MultipartFile file, String fileName) throws Exception {
        try (InputStream is = file.getInputStream()) {
            String contentType = file.getContentType();
            return minioClient.putObject(
                    PutObjectArgs.builder().bucket(path).object(fileName).stream(
                            is, -1, 10485760)
                            .contentType(contentType)
                            .build());
        }
    }

    public ObjectWriteResponse upload(MultipartFile file) throws Exception {
        return upload(file, file.getOriginalFilename());
    }

    public ObjectWriteResponse upload(File file) throws Exception {
        try (InputStream is = new FileInputStream(file)) {
            return minioClient.putObject(
                    PutObjectArgs.builder().bucket(path).object(file.getName()).stream(
                            is, -1, 10485760)
                            .build());
        }
    }

    public String getFileUrl(String objId, Integer expires, Boolean outside) throws Exception {
        String fileUrl =
                minioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.GET)
                                .bucket(path)
                                .object(objId)
                                .expiry(expires)
                                .build());
        fileUrl = outside ? StrUtil.replace(fileUrl, localUrl, publicUrl) : fileUrl;
        return fileUrl;
    }

    public InputStream getFile(String fileName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(path)
                        .object(fileName)
                        .build());
    }

    public void removeFile(String fileName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(path).object(fileName).build());
    }
}

package com.steven.willysbakeshop.service;

import com.steven.willysbakeshop.configuration.s3.S3Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;

@Service
public class S3Service  {
    private static final String BUCKET_NAME = "bakeshop-products";

    @Autowired
    S3Client s3Client;

    public URL  uploadFile(MultipartFile multipartFile) {
        try {
            final File file = convertMultiPartFileToFile(multipartFile);
            return uploadFileToS3Bucket(file);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private File convertMultiPartFileToFile(
            final MultipartFile multipartFile
    ) throws IOException {
        final File file = new File(multipartFile.getOriginalFilename());
        final FileOutputStream outputStream = new FileOutputStream(file);

        outputStream.write(multipartFile.getBytes());

        return file;
    }

    private URL uploadFileToS3Bucket(final File file) {
        final String key = LocalDateTime.now() + "_" + file.getName();
        final PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                .bucket(BUCKET_NAME)
                .key(key)
                .build();

        s3Client.getAmazonS3Client().putObject(putObjectRequest, RequestBody.fromFile(file));

        GetUrlRequest urlRequest = GetUrlRequest.builder().bucket(BUCKET_NAME).key(key).build();

        return s3Client.getAmazonS3Client().utilities().getUrl(urlRequest);
    }
}

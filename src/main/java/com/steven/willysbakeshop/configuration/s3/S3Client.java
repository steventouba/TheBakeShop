package com.steven.willysbakeshop.configuration.s3;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;

@Configuration
public class S3Client {
    private static final String ID = "AKIA2KQ2SULAAE6I3XJ2";
    private static final String KEY = "JWxwJGeSPI3c5CxPJr6rmS11O+2MUbfkGqKN3Sye";
    private static final Region REGION =  Region.US_WEST_1;

    @Bean
    public software.amazon.awssdk.services.s3.S3Client getAmazonS3Client() {
        final AwsBasicCredentials credentials = AwsBasicCredentials.create(ID, KEY);
        return software.amazon.awssdk.services.s3.S3Client
                .builder()
                .region(REGION)
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .build();
    }

}

package com.steven.willysbakeshop.configuration.s3;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;

public class S3Application {
    private static final AwsCredentials credentials;
    private static String bucketName;

    static {
        credentials = new AwsBasicCredentials()
    }
}

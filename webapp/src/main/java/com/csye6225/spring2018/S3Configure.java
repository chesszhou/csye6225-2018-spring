package com.csye6225.spring2018;

public class S3Configure {
    private String bucketName;
    private String accessKey;
    private String secretKey;

    public S3Configure() {

        this.bucketName = "s3.csye6225-spring2018-guobei.me";
        this.accessKey = "AKIAJYRJRH6MYNFWM5CA";
        this.secretKey = "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";

    }

    public String getBucketName() {
        return bucketName;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

}

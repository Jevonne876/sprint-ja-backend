package com.example.sprintjabackend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BucketName {
    BUCKET_NAME("sprint-ja-fileupload");
    private final String bucketName;
}
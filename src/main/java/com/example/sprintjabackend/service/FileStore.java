package com.example.sprintjabackend.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import com.example.sprintjabackend.enums.BucketName;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.Map;
import java.util.Optional;

import static com.example.sprintjabackend.enums.BucketName.BUCKET_NAME;

@AllArgsConstructor
@Service
public class FileStore {
    private final AmazonS3 amazonS3;

    public String uploadFile(MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            amazonS3.putObject(BUCKET_NAME.getBucketName(), file.getOriginalFilename(), file.getInputStream(), metadata);
            return  file.getOriginalFilename();
        } catch (IOException ioe) {

        } catch (AmazonServiceException serviceException) {
            throw serviceException;
        } catch (AmazonClientException clientException) {

            throw clientException;
        }
        return "File not uploaded: " + file.getOriginalFilename();
    }

    public byte[] downloadFile(String fileName) throws IOException {

        S3Object s3Object = amazonS3.getObject(BUCKET_NAME.getBucketName(),fileName);
        S3ObjectInputStream s3ObjectInputStream = s3Object.getObjectContent();

       return IOUtils.toByteArray(s3ObjectInputStream);
    }

    public FileOutputStream toFile(String filename) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(filename);
        fileOutputStream.write(downloadFile(filename));
        fileOutputStream.close();
        return fileOutputStream;
    }
}

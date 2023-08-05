package com.bid.idearush.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bid.idearush.global.exception.FileWriteException;
import com.bid.idearush.global.exception.errortype.FileWriteErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;

    public void delete(String filePath) {
        try {
            amazonS3Client.deleteObject(bucket, filePath);
        } catch (Exception ex) {
            throw new FileWriteException(FileWriteErrorCode.S3_NOT_DELETE);
        }
    }

    public void upload(String basePath, String fileName, MultipartFile multipartFile) {
        ObjectMetadata metadata= getObjectMetadataOf(multipartFile);
        String savePath = basePath + "/" + fileName;

        try {
            amazonS3Client.putObject(bucket, savePath, multipartFile.getInputStream(), metadata);
        } catch (Exception ex){
            throw new FileWriteException(FileWriteErrorCode.S3_NOT_WRITE);
        }
    }

    private ObjectMetadata getObjectMetadataOf(MultipartFile multipartFile){
        ObjectMetadata objectMetadata = new ObjectMetadata();

        objectMetadata.setContentType(multipartFile.getContentType());
        objectMetadata.setContentLength(multipartFile.getSize());

        return objectMetadata;
    }

}

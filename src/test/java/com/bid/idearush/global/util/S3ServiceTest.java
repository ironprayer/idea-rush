package com.bid.idearush.global.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.bid.idearush.global.exception.FileWriteException;
import com.bid.idearush.global.exception.errortype.FileWriteErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    @InjectMocks
    S3Service s3Service;
    @Mock
    AmazonS3Client amazonS3Client;
    @Mock
    MultipartFile multipartFile;


    @Test
    @DisplayName("S3 업로드 실패 테스트")
    void uploadInS3FailTest() throws IOException {
        given(multipartFile.getInputStream()).willThrow(IOException.class);

        FileWriteException ex = assertThrows(FileWriteException.class,
                () -> s3Service.upload("basePath", "fileName", multipartFile));

        assertEquals(FileWriteErrorCode.S3_NOT_WRITE.getStatus(), ex.getStatusCode());
        assertEquals(FileWriteErrorCode.S3_NOT_WRITE.getMsg(), ex.getMessage());
    }

    @Test
    @DisplayName("S3 업로드 성공 테스트")
    void uploadInS3SuccessTest() throws IOException {
        given(multipartFile.getInputStream()).willReturn(InputStream.nullInputStream());

        assertDoesNotThrow(() -> s3Service.upload("basePath", "fileName", multipartFile));
    }

}
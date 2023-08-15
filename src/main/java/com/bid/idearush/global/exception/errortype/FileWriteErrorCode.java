package com.bid.idearush.global.exception.errortype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum FileWriteErrorCode {

    S3_NOT_WRITE(HttpStatus.BAD_REQUEST, "S3 이미지 업로드에 실패했습니다"),
    S3_NOT_DELETE(HttpStatus.BAD_REQUEST, "S3 이미지 삭제에 실패했습니다"),
    NOT_IMAGE(HttpStatus.BAD_REQUEST, "이미지 파일이 아닙니다.")
    ;

    private final HttpStatus status;
    private final String msg;

}

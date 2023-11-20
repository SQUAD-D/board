package squad.board.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ListIterator;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class S3Service {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 폴더에 이미지 저장
    public String saveFile(MultipartFile multipartFile, String folderName) {
        String uuid = UUID.randomUUID().toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        try {
            s3Client.putObject(bucket, folderName + "/" + uuid, multipartFile.getInputStream(), metadata);
        } catch (IOException e) {
            log.error("Image Upload Failed");
        }
        return uuid;
    }

    public void moveImageToOriginal(String imageUUID, String from, String to) {
        s3Client.copyObject(bucket, "tmp/" + imageUUID, bucket, "original/" + imageUUID);
        s3Client.deleteObject(bucket, from + "/" + imageUUID);
    }

    // 이미지 소스 반환
    public String loadImage(String uuid, String folderName) {
        return s3Client.getUrl(bucket, folderName + "/" + uuid).toString();
    }

    // 단일 이미지 삭제
    public void deleteImage(String imageUUID, String folderName) {
        s3Client.deleteObject(bucket, folderName + "/" + imageUUID);
    }
}

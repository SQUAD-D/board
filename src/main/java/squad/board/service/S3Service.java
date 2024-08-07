package squad.board.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Slf4j
public class S3Service {

    private static final String TEMP_FOLDER_NAME = "tmp";
    private static final String ORIGINAL_FOLDER_NAME = "original";
    private final AmazonS3 s3Client;
    private final S3MessageQueue messageQueue;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 폴더에 이미지 저장
    public String saveFile(MultipartFile multipartFile, String folderName) {
        String uuid = UUID.randomUUID().toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());
        try {
            s3Client.putObject(bucket, folderName + "/" + uuid, multipartFile.getInputStream(),
                    metadata);
        } catch (IOException e) {
            log.error("Image Upload Failed");
        }
        return uuid;
    }

    @Async
    public void moveImageToOriginal(String uuid) {
        s3Client.copyObject(bucket, TEMP_FOLDER_NAME + "/" + uuid, bucket, ORIGINAL_FOLDER_NAME + "/" + uuid);
        s3Client.deleteObject(bucket, TEMP_FOLDER_NAME + "/" + uuid);
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

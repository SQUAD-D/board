package squad.board.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class S3Service {
    private final AmazonS3 s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 이미지 저장
    public String saveFile(MultipartFile multipartFile) throws IOException {
        String randomFileName = UUID.randomUUID().toString();

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        s3Client.putObject(bucket, randomFileName, multipartFile.getInputStream(), metadata);
        return randomFileName;
    }

    // 이미지 반환
    public String loadImage(String imageName) {
        if (imageName != null) {
            return s3Client.getUrl(bucket, imageName).toString();
        } else {
            return null;
        }
    }

    // 이미지 삭제
    public void deleteImage(String filename) {
        s3Client.deleteObject(bucket, filename);
    }
}

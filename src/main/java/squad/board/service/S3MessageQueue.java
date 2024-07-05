package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import squad.board.dto.S3.S3Task;
import squad.board.dto.board.ImageInfoRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3MessageQueue {

    private final RedisTemplate<String, Object> redisTemplate;
    private final S3FailMailSender s3FailMailSender;
    private static final String MESSAGE_QUEUE = "s3queue";

    public void pushAll(List<ImageInfoRequest> images) {
        List<S3Task> tasks = getS3Task(images);
        redisTemplate.opsForList().leftPushAll(MESSAGE_QUEUE, tasks);
    }

    public void push(S3Task task) {
        redisTemplate.opsForList().leftPush(MESSAGE_QUEUE, task);
    }

    public S3Task pop() {
        S3Task s3Task = (S3Task) redisTemplate.opsForList().rightPop(MESSAGE_QUEUE);
        if (s3Task.getFailCount() >= 3) {
            s3FailMailSender.send(s3Task.getImgUUID());
        }
        return s3Task;
    }

    public boolean isEmpty() {
        if (redisTemplate.opsForList().size(MESSAGE_QUEUE)==0) {
            return true;
        }
        return false;
    }

    private List<S3Task> getS3Task(List<ImageInfoRequest> images) {
        return images.stream()
                .map(ImageInfoRequest::toS3Task)
                .collect(Collectors.toList());
    }

}

package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class S3MessageQueue {

    private static final String MESSAGE_QUEUE = "s3queue";
    private final RedisTemplate<String, Object> redisTemplate;
    private final S3MailSender s3FailMailSender;


    public void pushAll(List<String> uuids) {
        for (String uuid : uuids) {
            redisTemplate.opsForList().leftPush(MESSAGE_QUEUE, uuid);
        }
    }

    public void push(String imageUUID) {
        redisTemplate.opsForList().leftPush(MESSAGE_QUEUE, imageUUID);
    }

    public String pop() {
        return (String) redisTemplate.opsForList().rightPop(MESSAGE_QUEUE);
    }

    public boolean isEmpty() {
        return redisTemplate.opsForList().size(MESSAGE_QUEUE)==0;
    }

}

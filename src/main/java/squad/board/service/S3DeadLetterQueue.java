package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class S3DeadLetterQueue {

    private static final String MESSAGE_QUEUE = "s3_dead_letter";
    private static final int EMPTY = 0;
    private static final int LIMIT = 30;
    private final RedisTemplate<String, Object> redisTemplate;

    public void push(String uuid) {
        redisTemplate.opsForList().leftPush(MESSAGE_QUEUE, uuid);
    }

    public void pushAll(List<String> imageUUID) {
        for (String uuid : imageUUID)
            redisTemplate.opsForList().leftPushAll(MESSAGE_QUEUE, uuid);
    }

    public String pop() {
        return (String) redisTemplate.opsForList().rightPop(MESSAGE_QUEUE);
    }

    public boolean isRateLimit() {
        return redisTemplate.opsForList().size(MESSAGE_QUEUE) > LIMIT;
    }

    public boolean isEmpty() {
        return redisTemplate.opsForList().size(MESSAGE_QUEUE)==EMPTY;
    }
}

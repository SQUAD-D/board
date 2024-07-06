package squad.board.service;

import com.amazonaws.AmazonServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3Consumer {

    private final S3MessageQueue messageQueue;
    private final S3DeadLetterQueue deadLetterQueue;
    private final S3Service s3Service;
    private final S3MailSender s3MailSender;
    private final String MAIL_CONTENT = "The dead letter queue has exceeded its maximum allowable size.";

//    @Scheduled(fixedDelay = 1000)
//    public void normalConsume() {
//        while (!messageQueue.isEmpty()) {
//            String uuid = messageQueue.pop();
//            try {
//                s3Service.moveImageToOriginal(uuid);
//            } catch (AmazonServiceException e) {
//                deadLetterQueue.push(uuid);
//            }
//        }
//    }

    @Scheduled(fixedDelay = 2000)
    public void deadLetterConsume() {
        if (deadLetterQueue.isRateLimit()) {
            s3MailSender.send(MAIL_CONTENT);
        }
        List<String> uuids = new ArrayList<>();
        while (!deadLetterQueue.isEmpty()) {
            uuids.add(deadLetterQueue.pop());
        }
        messageQueue.pushAll(uuids);
    }
}

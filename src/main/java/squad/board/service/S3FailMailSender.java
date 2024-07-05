package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3FailMailSender {
    private final JavaMailSender javaMailSender;
    private static final String ADMIN_EMAIL = "bukak2019@naver.com";
    private static final String TITLE = "S3 Move Action Failed";
    private static final String IMG_UUID = "imgUUID: ";

    public void send(final String imgUUID) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(ADMIN_EMAIL);
        message.setSubject(TITLE);
        message.setText(IMG_UUID + imgUUID);
        javaMailSender.send(message);
    }
}

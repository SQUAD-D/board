package squad.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class S3MailSender {
    private final JavaMailSender javaMailSender;
    private static final String ADMIN_EMAIL = "bukak2019@naver.com";
    private static final String TITLE = "S3 Move Action Failed";

    public void send(final String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(ADMIN_EMAIL);
        message.setSubject(TITLE);
        message.setText(content);
        javaMailSender.send(message);
    }
}

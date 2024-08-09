package org.bts.backend.util;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.bts.backend.repository.MailCertRedisRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MailProvider {
    // 메일인증절차 -> redis에 (email : UUID) 저장, TTL 10분
    // 확인 되면 (email : "ACK")로 변경
    // 회원가입시에 Redis 확인.
    private final JavaMailSender mailSender;
    private final MailCertRedisRepository mailCertRedisRepository;
    @Value("${mail.username}")
    private String adminName;

    public void sendMail(String email){
        // 메일 정보 구성.
        try {
            String uuid = UUID.randomUUID().toString();
            String from = adminName;
            String to = email;
            String subject = "BTS 회원가입 인증 메일입니다.";
            String content = "인증번호는 " + uuid + "입니다.";

            // Redis에 저장
            mailCertRedisRepository.save(email, uuid);

            // 메일 전송
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean checkMail(String email, String uuid) {
        // 인증번호 확인후 레디스값을 ACK로 변경
        if (mailCertRedisRepository.findByEmail(email).isPresent()) {
            if (mailCertRedisRepository.findByEmail(email).get().equals(uuid)) {
                mailCertRedisRepository.update(email, "ACK");
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }

    public boolean checkAck(String email) {
        // 마지막 ACK 확인 후 삭제
        if (mailCertRedisRepository.findByEmail(email).isPresent()) {
            if (mailCertRedisRepository.findByEmail(email).get().equals("ACK")) {
                mailCertRedisRepository.delete(email);
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
}

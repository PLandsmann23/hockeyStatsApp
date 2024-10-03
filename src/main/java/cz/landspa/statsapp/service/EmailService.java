package cz.landspa.statsapp.service;

import org.springframework.stereotype.Service;

@Service
public interface EmailService {
    void sendEmail(String replyTo, String to, String subject, String message);

    void sendVerificationEmail(String to, String token);
}

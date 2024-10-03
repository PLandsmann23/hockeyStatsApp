package cz.landspa.statsapp.service.impl;

import cz.landspa.statsapp.service.EmailService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailServiceImpl implements EmailService {

    final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;


    public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendEmail(String replyTo, String to, String subject, String message) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("noreply@patriklandsmann.cz");
            mailMessage.setReplyTo(replyTo);
            mailMessage.setTo(to);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);
            mailSender.send(mailMessage);
        } catch (Exception e){
            System.err.println("Chyba při odesílání e-mailu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void sendVerificationEmail(String to, String url) {
        try {
            Context context = new Context();
            String link = url;
            context.setVariable("link", link);
            String html = templateEngine.process("verificationEmail", context);
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, "UTF-8");
            mailMessage.setFrom("noreply <noreply@patriklandsmann.cz>");
            mailMessage.setTo(to);
            mailMessage.setSubject("Ověření účtu");
            mailMessage.setText(html, true);
            mailSender.send(mimeMessage);
        } catch (Exception e){
            System.err.println("Chyba při odesílání e-mailu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

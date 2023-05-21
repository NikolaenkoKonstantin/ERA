package com.app.server.era.backend.services;

import com.app.server.era.backend.dto.MessageSendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EraEmailService {
    @Value("${spring.mail.username}")
    private String emailFrom;
    private final JavaMailSender mailSender;


    public void send(String emailTo, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText("ERA: " + message);

        mailSender.send(mailMessage);
    }

    public String sendCodeToEmail(String emailTo){
        String code = generateCode();

        send(emailTo, "Подтверждение почты", code);

        return code;
    }

    private String generateCode(){
        String code = "";

        for(int i = 0; i < 4; i++) {
            code += (int) (Math.random() * 10);
        }

        return code;
    }

}

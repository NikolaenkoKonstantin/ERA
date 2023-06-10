package com.app.server.era.backend.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

//Сервис отправки сообщений
@Service
@RequiredArgsConstructor
public class EraEmailService {
    @Value("${spring.mail.username}")
    private String emailFrom;
    //Стандартный класс отправитель JavaMailSender
    private final JavaMailSender mailSender;

    @Async
    //Метод собственного сервиса
    public void send(String emailTo, String subject, String message) {
        //Создается объект простого сообщения
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        //Объект наполняется данными отправителя,
        // получателя, темы и сообщения
        mailMessage.setFrom(emailFrom);
        mailMessage.setTo(emailTo);
        mailMessage.setSubject(subject);
        mailMessage.setText("ERA: " + message);

        //вызов метода send из стандартного класса отправителя
        mailSender.send(mailMessage);
    }


    //Отправить код на почту
    public String sendCodeToEmail(String emailTo){
        String code = generateCode();

        send(emailTo, "Подтверждение почты", code);

        return code;
    }

    //Генерация кода
    private String generateCode(){
        String code = "";

        for(int i = 0; i < 4; i++) {
            code += (int) (Math.random() * 10);
        }

        return code;
    }
}
